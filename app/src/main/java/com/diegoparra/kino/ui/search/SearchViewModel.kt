package com.diegoparra.kino.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.diegoparra.kino.data.MoviesRepository
import com.diegoparra.kino.models.Movie
import com.diegoparra.kino.utils.fold
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val query = savedStateHandle.getLiveData(QUERY_SAVED_STATE_KEY, "").asFlow()

    fun setQuery(query: String) {
        savedStateHandle.set(QUERY_SAVED_STATE_KEY, query)
    }

    fun clearQuery() {
        savedStateHandle.set(QUERY_SAVED_STATE_KEY, "")
    }


    private val _moviesResult = MutableStateFlow<SearchResultState>(SearchResultState.EmptyQuery)
    val moviesResult: Flow<SearchResultState> = _moviesResult

    init {
        viewModelScope.launch {
            //  Map may have been used, but when trying to also emit loading in the same flow, it
            //  gets a little bit more complicated.
            query.collect { query ->
                _moviesResult.value = SearchResultState.Loading
                _moviesResult.value = if (query.isEmpty()) {
                    SearchResultState.EmptyQuery
                } else {
                    moviesRepository.searchMovieByName(query).fold(
                        onSuccess = { results ->
                            if (results.isEmpty()) {
                                SearchResultState.NoResults
                            } else {
                                SearchResultState.Success(results)
                            }
                        },
                        onFailure = { SearchResultState.Failure(it) }
                    )
                }
            }
        }
    }


    companion object {
        const val QUERY_SAVED_STATE_KEY = "query"
    }

}

sealed class SearchResultState {
    object Loading : SearchResultState()
    object EmptyQuery : SearchResultState()
    object NoResults : SearchResultState()
    class Success(val data: List<Movie>) : SearchResultState()
    class Failure(val exception: Exception) : SearchResultState()
}