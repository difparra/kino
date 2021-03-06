package com.diegoparra.kino.ui.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.diegoparra.kino.data.MoviesRepository
import com.diegoparra.kino.models.Genre
import com.diegoparra.kino.models.GenreWithMovies
import com.diegoparra.kino.utils.Event
import com.diegoparra.kino.utils.fold
import com.diegoparra.kino.utils.map
import com.diegoparra.kino.utils.toResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviesRepo: MoviesRepository
) : ViewModel() {


    //      ---------   Loading genres

    private val _failure = MutableStateFlow<Event<Throwable>?>(null)
    val failure: Flow<Event<Throwable>?> = _failure

    private val _loading = MutableStateFlow(false)
    val loading: Flow<Boolean> = _loading

    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    @VisibleForTesting
    val genres: Flow<List<Genre>> = _genres
    init {
        viewModelScope.launch {
            _loading.value = true
            moviesRepo.getGenres().fold(
                onSuccess = { _genres.value = it },
                onFailure = { _failure.value = Event(it) }
            )
            _loading.value = false
        }
    }

    //      ---------   Loading movies by genre

    private val _genresAndMovies = _genres.map { genres ->
        _loading.value = true
        val result = genres.map { genre ->
            moviesRepo.getMoviesByGenre(genre.id)
                .map { GenreWithMovies(genre, it) }
                .toResource()
        }
        _loading.value = false
        result
    }

    val genreAndMovies = _genresAndMovies



    //      ---------   OnMovieClick

    private val _navigateMovieDetails = MutableStateFlow<Event<String>?>(null)
    val navigateMovieDetails: Flow<Event<String>?> = _navigateMovieDetails

    fun onMovieClick(movieId: String) {
        _navigateMovieDetails.value = Event(movieId)
    }

}