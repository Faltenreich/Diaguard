package com.faltenreich.diaguard.main

import androidx.compose.material3.SnackbarDuration
import com.faltenreich.diaguard.navigation.screen.Screen

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