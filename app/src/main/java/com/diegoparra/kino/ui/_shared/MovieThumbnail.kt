package com.diegoparra.kino.ui._shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.diegoparra.kino.models.Movie
import com.diegoparra.kino.ui.MoviesFakes
import com.diegoparra.kino.ui._components.KinoImage
import com.diegoparra.kino.ui.theme.KinoTheme

private val MOVIE_HEIGHT = 150.dp

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MovieThumbnail(
    modifier: Modifier = Modifier,
    movie: Movie,
    height: Dp = MOVIE_HEIGHT,
    width: Dp = height * 2 / 3,
    onMovieClick: (movieId: String) -> Unit
) {
    Surface(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(5.dp))
            .clickable { onMovieClick(movie.id) }
    ) {
        KinoImage(
            modifier = Modifier.fillMaxSize(),
            imageUrl = movie.posterUrl
        )
    }
}


@Preview
@Composable
fun MovieThumbnailPreview() {
    KinoTheme {
        MovieThumbnail(
            movie = MoviesFakes.SuicideSquad,
            onMovieClick = {}
        )
    }
}