package com.faltenreich.diaguard.navigation.bar.snack

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState

class AndroidxSnackbarNavigation : SnackbarNavigation {

    lateinit var snackbarState: SnackbarHostState

    override suspend fun showSnackbar(
        message: String,
        actionLabel: String?,
        withDismissAction: Boolean,
        duration: SnackbarDuration,
    ) {
        snackbarState.showSnackbar(message, actionLabel, withDismissAction, duration)
    }
}