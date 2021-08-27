package com.diegoparra.kino

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.diegoparra.kino.ui.KinoApp
import com.diegoparra.kino.ui.theme.KinoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KinoTheme {
                KinoApp()
            }
        }
    }
}

