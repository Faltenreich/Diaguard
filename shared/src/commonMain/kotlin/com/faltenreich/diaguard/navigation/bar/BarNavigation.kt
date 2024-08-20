package com.faltenreich.diaguard.navigation.bar

import androidx.compose.material3.SnackbarDuration

interface BarNavigation {

    suspend fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration =
            if (actionLabel == null) SnackbarDuration.Short
            else SnackbarDuration.Indefinite,
    )
}