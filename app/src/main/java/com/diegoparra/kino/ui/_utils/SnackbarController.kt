package com.diegoparra.kino.ui._utils

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Class that deals with the coroutine scope.
 * With this class it is no longer necessary to call showSnackbar with scope.launch, but it is
 * convenient to call this method only from an onClick callback.
 * On the other hand, if same SnackbarController is used, that means that Snackbar can be hide
 * if another is displayed, so that second will be displayed immediately rather than queueing them,
 * waiting for the first to hide to show the second.
 */
class SnackbarController(
    private val scope: CoroutineScope,
    private val snackbarHostState: SnackbarHostState
) {

    private var currentJob: Job? = null

    fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        currentJob?.cancel()
        currentJob = scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = duration
            )
        }
    }

}