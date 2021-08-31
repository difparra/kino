package com.diegoparra.kino.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.diegoparra.kino.models.Genre
import com.diegoparra.kino.models.GenreWithMovies
import com.diegoparra.kino.models.Movie
import com.diegoparra.kino.ui.GenresFakes
import com.diegoparra.kino.ui.theme.Dimens
import com.diegoparra.kino.ui.utils.BasicErrorMessage
import com.diegoparra.kino.ui.utils.BasicLoading
import com.diegoparra.kino.utils.Resource
import com.diegoparra.kino.ui.utils.KinoImage

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToMovie: (movieId: String) -> Unit
) {
    //  Render loading state
    val loading by viewModel.loading.collectAsState(initial = false)
    if (loading) {
        BasicLoading()
    }

    //  Render success case
    val genreAndMovies by viewModel.genreAndMovies.collectAsState(initial = emptyList())
    GenreSectionsList(
        genreAndMovies = genreAndMovies,
        onMovieClick = viewModel::onMovieClick
    )

    //  Render failure case
    val failure by viewModel.failure.collectAsState(initial = null)
    failure?.getContentIfNotHandled()?.let {
        BasicErrorMessage(throwable = it)
    }

    //  Navigate to details screen
    val navigateMovieDetails by viewModel.navigateMovieDetails.collectAsState(initial = null)
    navigateMovieDetails?.getContentIfNotHandled()?.let { movieId ->
        navigateToMovie(movieId)
    }
}

@Composable
fun GenreSectionsList(
    genreAndMovies: List<Resource<GenreWithMovies>>,
    onMovieClick: (movieId: String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(Dimens.standard),
        verticalArrangement = Arrangement.spacedBy(Dimens.standard + Dimens.standard + Dimens.standard)
    ) {
        items(genreAndMovies) { it ->
            when (it) {
                is Resource.Loading -> CircularProgressIndicator()
                is Resource.Success -> GenreSection(
                    genre = it.data.genre,
                    movies = it.data.movies,
                    onMovieClick = onMovieClick
                )
                is Resource.Error -> {
                    Text(text = "Some failure has happened. \nClass: ${it.failure.javaClass}\nMessage: ${it.failure.message}")
                }
            }
        }
    }
}

@Composable
fun GenreSection(
    genre: Genre,
    movies: List<Movie>,
    onMovieClick: (movieId: String) -> Unit
) {
    Column {
        Text(text = genre.name, style = MaterialTheme.typography.subtitle1)
        Spacer(modifier = Modifier.height(Dimens.standard))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(Dimens.standard)
        ) {
            items(movies) { movie ->
                MovieThumbnail(movie = movie, onMovieClick = onMovieClick)
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MovieThumbnail(
    movie: Movie,
    onMovieClick: (movieId: String) -> Unit
) {
    Surface(
        modifier = Modifier
            .width(100.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(5.dp))
            .clickable { onMovieClick(movie.id) }
    ) {
        KinoImage(imageUrl = movie.posterUrl)
    }
}


@Preview(showSystemUi = true)
@Composable
fun GenreSectionsListPreview() {
    val genresAndMovies = GenresFakes.genreAndMovies
    val genresAndMoviesResource = genresAndMovies.map { Resource.Success(it) }
    GenreSectionsList(genreAndMovies = genresAndMoviesResource, onMovieClick = {})
}