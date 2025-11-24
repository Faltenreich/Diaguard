package com.faltenreich.diaguard.navigation

import androidx.compose.material3.SnackbarDuration

sealed interface NavigationEvent {

    data class PushScreen(
        val screen: com.faltenreich.diaguard.navigation.screen.Screen,
        val popHistory: Boolean,
    ) : com.faltenreich.diaguard.navigation.NavigationEvent

    data object PopScreen : com.faltenreich.diaguard.navigation.NavigationEvent

    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
        val withDismissAction: Boolean = false,
        val duration: SnackbarDuration =
            if (actionLabel == null) SnackbarDuration.Short
            else SnackbarDuration.Indefinite,
    ) : com.faltenreich.diaguard.navigation.NavigationEvent
}