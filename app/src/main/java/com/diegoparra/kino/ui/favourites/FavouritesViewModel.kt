package com.diegoparra.kino.ui.favourites

import androidx.lifecycle.ViewModel
import com.diegoparra.kino.data.MoviesRepository
import com.diegoparra.kino.models.Movie
import com.diegoparra.kino.utils.Event
import com.diegoparra.kino.utils.Resource
import com.diegoparra.kino.utils.toResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val moviesRepo: MoviesRepository
) : ViewModel() {

    val movies: Flow<Resource<List<Movie>>> =
        moviesRepo.getFavourites()
            .map {
                it.toResource()
            }
            .onStart { emit(Resource.Loading) }


    private val _navigateMovieDetails = MutableStateFlow<Event<String>?>(null)
    val navigateMovieDetails: Flow<Event<String>?> = _navigateMovieDetails

    fun onMovieClick(movieId: String) {
        _navigateMovieDetails.value = Event(movieId)
    }

}