package com.faltenreich.diaguard.navigation

import androidx.compose.material3.SnackbarDuration
import com.faltenreich.diaguard.navigation.screen.Screen

sealed interface NavigationEvent {

    data class PushScreen(val screen: Screen, val popHistory: Boolean = false) : NavigationEvent

    data class PopScreen(val result: Pair<String, Any>?) : NavigationEvent

    data class ShowSnackbar(
        val message: String,
        val actionLabel: String?,
        val withDismissAction: Boolean,
        val duration: SnackbarDuration,
    ) : NavigationEvent
}