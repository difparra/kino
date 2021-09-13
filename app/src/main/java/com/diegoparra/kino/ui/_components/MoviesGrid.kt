package com.diegoparra.kino.ui._components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.diegoparra.kino.models.Movie
import com.diegoparra.kino.ui._utils.MoviesFakes
import com.diegoparra.kino.ui.theme.Dimens
import com.diegoparra.kino.ui.theme.KinoTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoviesGrid(
    movies: List<Movie>,
    columns: Int = 3,
    onMovieClick: (movieId: String) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize().padding(Dimens.small),
        cells = GridCells.Fixed(columns)
    ) {
        items(movies) { movie ->
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
fun MoviesGridPreview() {
    KinoTheme {
        MoviesGrid(
            movies = listOf(
                MoviesFakes.SuicideSquad,
                MoviesFakes.JungleCruise,
                MoviesFakes.SuicideSquad,
                MoviesFakes.JungleCruise
            ),
            onMovieClick = {}
        )
    }
}