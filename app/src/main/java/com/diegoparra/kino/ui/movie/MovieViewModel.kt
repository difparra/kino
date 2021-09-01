package com.diegoparra.kino.ui.movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoparra.kino.data.MoviesRepository
import com.diegoparra.kino.models.Genre
import com.diegoparra.kino.models.Movie
import com.diegoparra.kino.utils.Resource
import com.diegoparra.kino.utils.toResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val moviesRepo: MoviesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _movieId = savedStateHandle.get<String>(MOVIE_ID_KEY)!!

    //      ----------      MOVIE       ------------------------------------------------------------

    private val _movie = MutableStateFlow<Resource<Movie>>(Resource.Loading)
    val movie: Flow<Resource<Movie>> = _movie

    init {
        viewModelScope.launch {
            _movie.value = Resource.Loading
            _movie.value = moviesRepo.getMovieById(_movieId).toResource()
        }
    }

    //      ----------      GENRES NAMES        ----------------------------------------------------

    private val _genres = MutableStateFlow<List<Genre>>(emptyList())

    init {
        viewModelScope.launch {
            moviesRepo.getGenres().fold(
                onSuccess = { _genres.value = it },
                onFailure = { Timber.e("Failure loading genres in movie details\n$it\n${it.printStackTrace()}") }
            )
        }
    }

    val genreNames = _movie.combine(_genres) { movie, genres ->
        if (movie is Resource.Success) {
            movie.data.genreIds.mapNotNull { genreId ->
                genres.find { it.id == genreId }?.name
            }
        } else {
            emptyList()
        }
    }


    //      ----------      IS FAVOURITE        ----------------------------------------------------

    val isFavourite: Flow<Boolean> =
        moviesRepo.isFavourite(_movieId)
            .map { it.getOrDefault(false) }

    fun toggleFavourite() {
        viewModelScope.launch {
            moviesRepo.toggleFavourite(_movieId)
        }
    }


    companion object {
        const val MOVIE_ID_KEY = "movieId"
    }
}