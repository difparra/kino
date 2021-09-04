package com.diegoparra.kino.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diegoparra.kino.R
import com.diegoparra.kino.models.Movie
import com.diegoparra.kino.ui.MoviesFakes
import com.diegoparra.kino.ui._components.SearchBar
import com.diegoparra.kino.ui._components.SimpleRatingBar
import com.diegoparra.kino.ui._utils.BasicErrorMessage
import com.diegoparra.kino.ui._utils.BasicLoading
import com.diegoparra.kino.ui._utils.KinoImage
import com.diegoparra.kino.ui.theme.Dimens
import com.diegoparra.kino.ui.theme.KinoTheme

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    navigateToMovie: (movieId: String) -> Unit
) {
    val query by viewModel.query.collectAsState(initial = "")
    val result by viewModel.moviesResult.collectAsState(initial = SearchResultState.EmptyQuery)

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.small),
            text = query,
            onTextChanged = viewModel::setQuery,
            onSearch = { },
            onClear = viewModel::clearQuery
        )

        when (result) {
            SearchResultState.Loading -> BasicLoading()
            SearchResultState.EmptyQuery -> BasicErrorMessage(errorMessage = stringResource(id = R.string.empty_query))
            SearchResultState.NoResults -> BasicErrorMessage(errorMessage = stringResource(id = R.string.search_no_results))
            is SearchResultState.Success -> SearchResults(
                movies = (result as SearchResultState.Success).data,
                onMovieClick = navigateToMovie
            )
            is SearchResultState.Failure -> BasicErrorMessage(throwable = (result as SearchResultState.Failure).exception)
        }
    }
}

@Composable
private fun SearchResults(movies: List<Movie>, onMovieClick: (String) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(Dimens.big),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(Dimens.big),
        modifier = Modifier.fillMaxSize()
    ) {
        items(movies) { movie ->
            MovieSearchResult(movie = movie, onMovieClick = onMovieClick)
        }
    }
}

@Composable
private fun MovieSearchResult(movie: Movie, onMovieClick: (String) -> Unit) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .clickable { onMovieClick(movie.id) },
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KinoImage(
                imageUrl = movie.posterUrl,
                modifier = Modifier
                    .fillMaxWidth(0.2f),
                contentScale = ContentScale.FillWidth
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.standard)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.subtitle1
                )
                movie.year?.let {
                    Spacer(modifier = Modifier.height(Dimens.small))
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(
                            text = "($it)",
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
                movie.rating?.let {
                    Spacer(modifier = Modifier.height(Dimens.small))
                    SimpleRatingBar(
                        ratingPercent = it,
                        width = 70.dp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchResultsPreview() {
    KinoTheme {
        SearchResults(
            movies = listOf(MoviesFakes.SuicideSquad, MoviesFakes.JungleCruise),
            onMovieClick = {}
        )
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    KinoTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                text = "query",
                onTextChanged = { },
                onSearch = { },
                onClear = { }
            )

            SearchResults(
                movies = listOf(
                    MoviesFakes.SuicideSquad,
                    MoviesFakes.JungleCruise,
                    MoviesFakes.SuicideSquad,
                    MoviesFakes.JungleCruise,
                ),
                onMovieClick = { }
            )
        }
    }
}
