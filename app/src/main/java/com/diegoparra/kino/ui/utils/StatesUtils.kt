package com.diegoparra.kino.ui.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import timber.log.Timber

@Composable
fun AlignedInParent(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        contentAlignment = alignment,
        modifier = modifier.fillMaxSize(),
        content = content
    )
}

@Composable
fun BasicLoading() {
    AlignedInParent(alignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}


@Composable
fun BasicErrorMessage(throwable: Throwable) {
    BasicErrorMessage(errorMessage = throwable.message ?: "Some error has happened")
    Timber.e("Failure = $throwable\nClass = ${throwable.javaClass}\nMessage = ${throwable.message}")
}

@Composable
fun BasicErrorMessage(errorMessage: String) {
    AlignedInParent(alignment = Alignment.Center) {
        Text(text = errorMessage)
    }
}