package com.faltenreich.diaguard.navigation

import androidx.compose.material3.SnackbarDuration

class ShowSnackbarUseCase(
    private val navigation: Navigation,
) {

    suspend operator fun invoke(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration =
            if (actionLabel == null) SnackbarDuration.Short
            else SnackbarDuration.Indefinite,
    ) {
        navigation.showSnackbar(message, actionLabel, withDismissAction, duration)
    }
}