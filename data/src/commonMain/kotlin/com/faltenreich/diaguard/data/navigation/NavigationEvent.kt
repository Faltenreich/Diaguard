package com.faltenreich.diaguard.data.navigation

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