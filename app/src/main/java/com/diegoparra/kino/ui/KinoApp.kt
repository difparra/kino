package com.diegoparra.kino.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.diegoparra.kino.ui.navigation.KinoBottomNavBar
import com.diegoparra.kino.ui.navigation.KinoNavGraph
import com.diegoparra.kino.ui.theme.KinoTheme

@Composable
fun KinoApp() {
    KinoTheme {
        val navController = rememberNavController()
        val scaffoldState = rememberScaffoldState()
        Scaffold(
            scaffoldState = scaffoldState,
            bottomBar = { KinoBottomNavBar(navController = navController) }
        ) { innerPaddingModifier ->
            KinoNavGraph(
                navController = navController,
                scaffoldState = scaffoldState,
                modifier = Modifier.padding(innerPaddingModifier)
            )
        }

    }
}