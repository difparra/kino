package com.diegoparra.kino.ui.favourites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.diegoparra.kino.models.Movie
import com.diegoparra.kino.ui._utils.BasicErrorMessage
import com.diegoparra.kino.ui._utils.BasicLoading
import com.diegoparra.kino.ui._components.MoviesGrid
import com.diegoparra.kino.utils.Resource

@Composable
fun FavouritesScreen(
    viewModel: FavouritesViewModel,
    navigateToMovie: (movieId: String) -> Unit
) {
    //  Render movies
    val movies by viewModel.movies.collectAsState(initial = Resource.Loading)
    when (movies) {
        is Resource.Loading -> BasicLoading()
        is Resource.Success ->
            MoviesGrid(
                movies = (movies as Resource.Success<List<Movie>>).data,
                onMovieClick = viewModel::onMovieClick
            )
        is Resource.Error -> BasicErrorMessage(throwable = (movies as Resource.Error).failure)
    }

    //  Navigate to movie details
    val navigateMoviesDetail by viewModel.navigateMovieDetails.collectAsState(initial = null)
    navigateMoviesDetail?.getContentIfNotHandled()?.let { movieId ->
        navigateToMovie(movieId)
    }
}