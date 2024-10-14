package com.faltenreich.diaguard.navigation.bar.snack

import androidx.compose.material3.SnackbarDuration

interface SnackbarNavigation {

    suspend fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration =
            if (actionLabel == null) SnackbarDuration.Short
            else SnackbarDuration.Indefinite,
    )
}