package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.screen.Screen

sealed interface NavigationIntent {

    data class OpenBottomSheet(val screen: Screen) : NavigationIntent

    data object CloseBottomSheet : NavigationIntent
}