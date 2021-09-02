package com.diegoparra.kino.ui.favourites

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.diegoparra.kino.models.Movie
import com.diegoparra.kino.ui.MoviesFakes
import com.diegoparra.kino.ui._components.BasicErrorMessage
import com.diegoparra.kino.ui._components.BasicLoading
import com.diegoparra.kino.ui._shared.MovieThumbnail
import com.diegoparra.kino.ui.theme.Dimens
import com.diegoparra.kino.ui.theme.KinoTheme
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
            FavouritesScreen(
                favMovies = (movies as Resource.Success<List<Movie>>).data,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavouritesScreen(
    favMovies: List<Movie>,
    onMovieClick: (movieId: String) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize().padding(Dimens.small),
        cells = GridCells.Fixed(3)
    ) {
        items(favMovies) { movie ->
            MovieThumbnail(
                modifier = Modifier.padding(Dimens.standard),
                movie = movie,
                onMovieClick = onMovieClick
            )
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun FavouritesScreenPreview() {
    KinoTheme {
        FavouritesScreen(
            favMovies = listOf(
                MoviesFakes.SuicideSquad,
                MoviesFakes.JungleCruise,
                MoviesFakes.SuicideSquad,
                MoviesFakes.JungleCruise
            ),
            onMovieClick = {}
        )
    }
}