package com.diegoparra.kino.ui.movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoparra.kino.data.MoviesRepository
import com.diegoparra.kino.models.Movie
import com.diegoparra.kino.utils.Resource
import com.diegoparra.kino.utils.toResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val moviesRepo: MoviesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _movieId = savedStateHandle.get<String>(MOVIE_ID_KEY)!!

    private val _movie = MutableStateFlow<Resource<Movie>>(Resource.Loading)
    val movie: Flow<Resource<Movie>> = _movie

    init {
        viewModelScope.launch {
            _movie.value = Resource.Loading
            _movie.value = moviesRepo.getMovieById(_movieId).toResource()
        }
    }


    companion object {
        const val MOVIE_ID_KEY = "movieId"
    }
}