package com.diegoparra.kino.viewmodels

import androidx.lifecycle.*
import com.diegoparra.kino.data.MoviesRepository
import com.diegoparra.kino.models.Genre
import com.diegoparra.kino.models.GenreWithMovies
import com.diegoparra.kino.utils.Event
import com.diegoparra.kino.utils.toResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepo: MoviesRepository
) : ViewModel() {

    private val _failure = MutableLiveData<Event<Throwable>>()
    val failure: LiveData<Event<Throwable>> = _failure

    private val _genres = MutableStateFlow<List<Genre>>(emptyList())

    init {
        viewModelScope.launch {
            moviesRepo.getGenres().fold(
                onSuccess = { _genres.value = it },
                onFailure = { _failure.value = Event(it) }
            )
        }
    }

    private val _genresAndMovies = _genres.map { genres ->
        genres.map { genre ->
            moviesRepo.getMoviesByGenre(genre.id)
                .map { GenreWithMovies(genre, it) }
                .toResource()
        }
    }
    val genreAndMovies = _genresAndMovies.asLiveData()

}