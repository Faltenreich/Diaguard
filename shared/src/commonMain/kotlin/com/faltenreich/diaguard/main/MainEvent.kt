package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.data.navigation.SnackbarDuration
import com.faltenreich.diaguard.data.navigation.Screen

sealed interface MainEvent {

    data class NavigateTo(
        val screen: Screen,
        val clearHistory: Boolean,
    ) : MainEvent

    data object NavigateBack : MainEvent

    data class ShowSnackbar(
        val message: String,
        val actionLabel: String?,
        val withDismissAction: Boolean,
        val duration: SnackbarDuration,
    ) : MainEvent
}