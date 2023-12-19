package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.screen.Screen

sealed interface NavigationIntent {

    data class NavigateTo(val screen: Screen, val clearBackStack: Boolean) : NavigationIntent
}