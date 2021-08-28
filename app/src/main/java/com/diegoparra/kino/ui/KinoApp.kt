package com.diegoparra.kino.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.diegoparra.kino.ui.screens.MoviesScreen
import com.diegoparra.kino.ui.theme.KinoTheme

@Composable
fun KinoApp() {
    // A surface container using the 'background' color from the theme
    Surface(color = MaterialTheme.colors.background) {
        MoviesScreen()
    }
}