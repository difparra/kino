package com.diegoparra.kino.ui.movie

import androidx.annotation.IntRange
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.diegoparra.kino.models.Movie
import com.diegoparra.kino.ui.theme.Dimens
import com.diegoparra.kino.utils.Resource
import com.diegoparra.kino.R
import com.diegoparra.kino.ui.MoviesFakes
import com.diegoparra.kino.ui.theme.ColorControl
import com.diegoparra.kino.ui.theme.KinoTheme
import com.diegoparra.kino.ui.utils.*

@Composable
fun MovieScreen(
    viewModel: MovieViewModel,
    navigateUp: () -> Unit
) {
    val movieResource by viewModel.movie.collectAsState(initial = Resource.Loading)
    val genreNames by viewModel.genreNames.collectAsState(initial = emptyList())

    when (movieResource) {
        is Resource.Loading -> BasicLoading()
        is Resource.Success -> MovieScreen(
            movie = (movieResource as Resource.Success<Movie>).data,
            genreNames = genreNames,
            navigateUp = navigateUp
        )
        is Resource.Error -> BasicErrorMessage(throwable = (movieResource as Resource.Error).failure)
    }
}

@Composable
fun MovieScreen(
    movie: Movie,
    genreNames: List<String>,
    navigateUp: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val scrollState = rememberScrollState(0)
        AppBar(backdropUrl = movie.backdropUrl, title = movie.title, navigateUp = navigateUp)
        Body(movie = movie, genreNames = genreNames, scroll = scrollState)
    }
}

@Composable
private fun AppBar(backdropUrl: String, title: String, navigateUp: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        ImageCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            imageUrl = backdropUrl,
            title = title
        )
        NavigateUpButton(upPress = navigateUp)
    }
}

@Composable
private fun NavigateUpButton(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .padding(Dimens.standard)
            .size(36.dp)
            .background(color = ColorControl.copy(alpha = 0.25f), shape = CircleShape)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            tint = Color.White,
            contentDescription = stringResource(id = R.string.label_back)
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun ImageCard(modifier: Modifier = Modifier, imageUrl: String, title: String) {
    Box(modifier = modifier) {
        KinoImage(
            imageUrl = imageUrl,
            contentScale = ContentScale.Crop,
            contentDescription = title
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        0.7f to Color.Transparent,
                        1.0f to Color.Black
                    )
                )
        )
        AlignedInParent(
            alignment = Alignment.BottomStart,
            modifier = Modifier.padding(Dimens.big)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                color = Color.White
            )
        }
    }
}

@Composable
private fun Body(movie: Movie, genreNames: List<String>, scroll: ScrollState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scroll)
            .padding(Dimens.big)
    ) {
        MovieStatsAndDetails(
            rating = movie.rating,
            runtime = movie.runtimeMinutes,
            year = movie.year,
            genres = genreNames
        )
        Spacer(modifier = Modifier.height(Dimens.big))
        Overview(overview = movie.overview)
    }
}

@Composable
fun MovieStatsAndDetails(
    modifier: Modifier = Modifier,
    @IntRange(from = 0, to = 100) rating: Int?,
    runtime: Int?,
    year: Int?,
    genres: List<String>
) {
    Column(modifier = modifier) {
        rating?.let {
            SimpleRatingBar(ratingPercent = it)
            Spacer(modifier = Modifier.height(Dimens.standard))
        }
        val runtimeStr = runtime?.let {
            val hours = (it / 60).let { if (it > 0) it else null }
            val minutes = it % 60
            hours?.let { "${it}h " } ?: "" + "${minutes}m"
        }
        Text(
            text = "$year • $runtimeStr • ${genres.joinToString()}",
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
fun Overview(modifier: Modifier = Modifier, overview: String) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.overview),
            style = MaterialTheme.typography.subtitle1
        )
        Spacer(modifier = Modifier.height(Dimens.standard))
        var expanded by remember { mutableStateOf(false) }
        Text(
            text = overview,
            style = MaterialTheme.typography.body2,
            maxLines = if (expanded) Int.MAX_VALUE else 5,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .clickable { expanded = !expanded }
                .animateContentSize()
        )
    }
}

@Preview
@Composable
fun MovieScreenPreview() {
    KinoTheme {
        MovieScreen(
            movie = MoviesFakes.SuicideSquad,
            genreNames = listOf("Action", "Adventure", "Comedy"),
            navigateUp = {})
    }
}