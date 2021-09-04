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


    /*
//        Simplest option: It is working, but it does not define loading state.
//        It could be defined in another variable, but does not seem to be right to have two variables
//        defining the SearchResultState when I can have just one.
//        Another option may be using flatMapLatest, and defining the request to repository inside a
//        flow, that can add onStart emitting a loading state value. It is a valid option, and is
//        great if data from repo is directly coming as flow, but if it is coming as a single value,
//        from a suspend function, it may be better to simply collect the queryFlow, and defined values
//        of _moviesResult MutableStateFlow in there.

    val moviesResult = query
        .map { query ->
            if (query.isEmpty()) {
                Timber.d("returning query is empty")
                SearchResultState.EmptyQuery
            } else {
                moviesRepository.searchMovieByName(query).fold(
                    onSuccess = { results ->
                        if (results.isEmpty()) {
                            Timber.d("returning no results")
                            SearchResultState.NoResults
                        } else {
                            Timber.d("returning results")
                            SearchResultState.Success(results)
                        }
                    },
                    onFailure = {
                        Timber.d("returning failure")
                        SearchResultState.Failure(it)
                    }
                )
            }
        }
     */


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