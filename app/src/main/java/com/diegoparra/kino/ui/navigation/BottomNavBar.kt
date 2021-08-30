package com.diegoparra.kino.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.diegoparra.kino.R

sealed class BottomNavBarDestinations(
    val route: String,
    @StringRes val title: Int,
    val icon: ImageVector
) {
    object HOME : BottomNavBarDestinations(
        MainDestinations.HOME_ROUTE,
        R.string.menu_home,
        Icons.Default.Home
    )

    object SEARCH : BottomNavBarDestinations(
        MainDestinations.SEARCH_ROUTE,
        R.string.menu_search,
        Icons.Default.Search
    )

    object FAVOURITES : BottomNavBarDestinations(
        MainDestinations.FAVOURITES_ROUTE,
        R.string.menu_favs,
        Icons.Default.Star
    )

    companion object {
        val items = listOf(HOME, SEARCH, FAVOURITES)
    }
}

@Composable
fun KinoBottomNavBar(navController: NavHostController) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        BottomNavBarDestinations.items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = null) },
                label = { Text(text = stringResource(id = screen.title)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}
