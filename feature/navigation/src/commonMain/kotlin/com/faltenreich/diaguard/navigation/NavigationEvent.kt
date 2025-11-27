package com.faltenreich.diaguard.navigation

import androidx.compose.material3.SnackbarDuration

sealed interface NavigationEvent {

    data class NavigateTo(
        val target: NavigationTarget,
        val clearHistory: Boolean,
    ) : NavigationEvent

    data object NavigateBack : NavigationEvent

    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
        val withDismissAction: Boolean = false,
        val duration: SnackbarDuration =
            if (actionLabel == null) SnackbarDuration.Short
            else SnackbarDuration.Indefinite,
    ) : NavigationEvent
}