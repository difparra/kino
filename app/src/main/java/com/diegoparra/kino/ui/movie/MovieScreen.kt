package com.diegoparra.kino.ui.movie

import androidx.compose.foundation.layout.Column
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.diegoparra.kino.models.Movie
import com.diegoparra.kino.utils.Resource

@Composable
fun MovieScreen(
    viewModel: MovieViewModel,
    navigateBack: () -> Unit
) {
    val movieResource by viewModel.movie.collectAsState(initial = Resource.Loading)

    when (movieResource) {
        is Resource.Loading -> CircularProgressIndicator()
        is Resource.Success -> MovieScreen(movie = (movieResource as Resource.Success<Movie>).data)
        is Resource.Error -> {
            val message = (movieResource as Resource.Error<Movie>).failure.message
            Text(text = message ?: "Some error has happened")
        }
    }
}

@Composable
fun MovieScreen(
    movie: Movie
) {
    Column {
        Text(text = "MovieId = ${movie.id}")
        Text(text = movie.title)
        Text(text = movie.overview)
    }
}
