package com.diegoparra.kino.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.diegoparra.kino.models.Genre
import com.diegoparra.kino.models.GenreWithMovies
import com.diegoparra.kino.models.Movie
import com.diegoparra.kino.utils.Resource
import com.diegoparra.kino.viewmodels.MoviesViewModel

@Composable
fun MoviesScreen() {

    val viewModel: MoviesViewModel = viewModel()
    val genreAndMovies by viewModel.genreAndMovies.observeAsState(emptyList())
    val failure by viewModel.failure.observeAsState(null)

    Movies(genreAndMovies = genreAndMovies)

    failure?.getContentIfNotHandled()?.let {
        Text(text = "Some failure has happened. \nClass: ${it.javaClass}\nMessage: ${it.message}")
    }
}


@Composable
fun Movies(genreAndMovies: List<Resource<GenreWithMovies>>) {
    LazyColumn {
        genreAndMovies.forEach {
            item {
                when (it) {
                    is Resource.Loading -> CircularProgressIndicator()
                    is Resource.Success -> MoviesByGenre(
                        genre = it.data.genre,
                        movies = it.data.movies,
                        onMovieClick = {})
                    is Resource.Error -> {
                        Text(text = "Some failure has happened. \nClass: ${it.failure.javaClass}\nMessage: ${it.failure.message}")
                    }
                }
            }
        }
    }
}


@Composable
fun MoviesByGenre(
    genre: Genre,
    movies: List<Movie>,
    onMovieClick: (movieId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(text = genre.name)
        LazyRow {
            movies.forEach {
                item { Movie(movie = it, onItemClick = onMovieClick) }
            }
        }
    }
}

@Composable
fun Movie(
    movie: Movie,
    onItemClick: (movieId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.clickable { onItemClick(movie.id) }) {
        Text(text = movie.title)
    }
}

