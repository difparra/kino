package com.diegoparra.kino.ui.movie

import androidx.annotation.IntRange
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
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
import com.diegoparra.kino.ui._components.*

private val APP_BAR_HEIGHT = 250.dp
private val FAB_SIZE = 56.dp

@Composable
fun MovieScreen(
    viewModel: MovieViewModel,
    scaffoldState: ScaffoldState,
    navigateUp: () -> Unit
) {
    val movieResource by viewModel.movie.collectAsState(initial = Resource.Loading)
    val genreNames by viewModel.genreNames.collectAsState(initial = emptyList())
    val isFavourite by viewModel.isFavourite.collectAsState(initial = false)

    when (movieResource) {
        is Resource.Loading -> BasicLoading()
        is Resource.Success -> MovieScreen(
            movie = (movieResource as Resource.Success<Movie>).data,
            genreNames = genreNames,
            isFavourite = isFavourite,
            scaffoldState = scaffoldState,
            toggleFavourite = viewModel::toggleFavourite,
            navigateUp = navigateUp
        )
        is Resource.Error -> BasicErrorMessage(throwable = (movieResource as Resource.Error).failure)
    }
}

@Composable
fun MovieScreen(
    movie: Movie,
    genreNames: List<String>,
    isFavourite: Boolean,
    scaffoldState: ScaffoldState,
    toggleFavourite: () -> Unit,
    navigateUp: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            val scrollState = rememberScrollState(0)
            AppBar(
                backdropUrl = movie.backdropUrl,
                title = movie.title,
                navigateUp = navigateUp
            )
            Body(movie = movie, genreNames = genreNames, scroll = scrollState)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 0.dp, y = APP_BAR_HEIGHT - (FAB_SIZE / 2))
                .padding(horizontal = Dimens.big, vertical = 0.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            FavouriteButton(
                scaffoldState = scaffoldState,
                isFavourite = isFavourite,
                toggleFavourite = toggleFavourite
            )
        }
    }
}

@Composable
private fun AppBar(
    modifier: Modifier = Modifier,
    backdropUrl: String,
    title: String,
    navigateUp: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(APP_BAR_HEIGHT)
    ) {
        ImageCard(
            modifier = Modifier.matchParentSize(),
            imageUrl = backdropUrl,
            title = title
        )
        NavigateUpButton(
            modifier = Modifier.align(Alignment.TopStart),
            upPress = navigateUp
        )
    }
}

@Composable
private fun NavigateUpButton(modifier: Modifier = Modifier, upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = modifier
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
            modifier = Modifier.matchParentSize(),
            imageUrl = imageUrl,
            contentScale = ContentScale.Crop,
            contentDescription = title
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        0.7f to Color.Transparent,
                        1.0f to Color.Black
                    )
                )
        )
        Text(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(Dimens.big),
            text = title,
            style = MaterialTheme.typography.h5,
            color = Color.White
        )
    }
}

@Composable
private fun FavouriteButton(
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier,
    isFavourite: Boolean,
    toggleFavourite: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarController = remember { SnackbarController(scope, scaffoldState.snackbarHostState) }
    val addedMessage = stringResource(id = R.string.added_to_favourites)
    val removedMessage = stringResource(id = R.string.removed_from_favourites)

    FloatingActionButton(
        modifier = modifier.size(FAB_SIZE),
        onClick = {
            toggleFavourite()
            //  Message will be the opposite as isFavourite is the same for this composable.
            //  The new is favourite will be in another composable, when this is call to recompose
            //  as its arguments have changed.
            snackbarController.showSnackbar(
                message = if (isFavourite) removedMessage else addedMessage
            )
        }
    ) {
        Icon(
            imageVector = if (isFavourite) Icons.Default.Star else Icons.Default.StarOutline,
            contentDescription = stringResource(id = R.string.menu_favs)
        )
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
            isFavourite = false,
            scaffoldState = rememberScaffoldState(),
            toggleFavourite = {},
            navigateUp = {})
    }
}