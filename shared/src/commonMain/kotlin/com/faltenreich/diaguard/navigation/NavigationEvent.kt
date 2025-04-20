package com.faltenreich.diaguard.navigation

import androidx.compose.material3.SnackbarDuration
import com.faltenreich.diaguard.navigation.screen.Screen

sealed interface NavigationEvent {

    data class PushScreen(val screen: Screen, val popHistory: Boolean) : NavigationEvent

    data object PopScreen : NavigationEvent

    data class OpenBottomSheet(val bottomSheet: Screen) : NavigationEvent

    data object CloseBottomSheet : NavigationEvent

    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
        val withDismissAction: Boolean = false,
        val duration: SnackbarDuration =
            if (actionLabel == null) SnackbarDuration.Short
            else SnackbarDuration.Indefinite,
    ) : NavigationEvent
}