package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.bar.snackbar.SnackbarDuration

sealed interface NavigationEvent {

    data class NavigateTo(
        val target: NavigationTarget,
        val clearHistory: Boolean,
    ) : NavigationEvent

    data object NavigateBack : NavigationEvent

    data class ShowSnackbar(
        val message: String,
        val actionLabel: String?,
        val withDismissAction: Boolean,
        val duration: SnackbarDuration,
    ) : NavigationEvent
}