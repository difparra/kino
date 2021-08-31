package com.diegoparra.kino.ui.navigation

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.diegoparra.kino.ui.home.HomeScreen
import com.diegoparra.kino.ui.home.HomeViewModel
import com.diegoparra.kino.ui.movie.MovieScreen
import com.diegoparra.kino.ui.movie.MovieViewModel
import com.diegoparra.kino.ui.movie.MovieViewModel.Companion.MOVIE_ID_KEY

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val SEARCH_ROUTE = "search"
    const val FAVOURITES_ROUTE = "favourites"
    const val MOVIE_ROUTE = "movie"
}

class MainActions(navController: NavHostController) {
    val navigateToMovie: (String) -> Unit = { movieId: String ->
        navController.navigate("${MainDestinations.MOVIE_ROUTE}/$movieId")
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}


@Composable
fun KinoNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainDestinations.HOME_ROUTE
) {

    val actions = remember(navController) { MainActions(navController) }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(MainDestinations.HOME_ROUTE) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = homeViewModel,
                navigateToMovie = actions.navigateToMovie
            )
        }
        composable(MainDestinations.SEARCH_ROUTE) {
            Text(text = "Search screen")
        }
        composable(MainDestinations.FAVOURITES_ROUTE) {
            Text(text = "Favourites Screen")
        }
        composable(
            route = "${MainDestinations.MOVIE_ROUTE}/{$MOVIE_ID_KEY}",
            arguments = listOf(navArgument(MOVIE_ID_KEY) { type = NavType.StringType })
        ) {
            val movieViewModel: MovieViewModel = hiltViewModel()
            MovieScreen(
                viewModel = movieViewModel,
                navigateUp = actions.upPress
            )
        }

    }

}


