package com.faltenreich.diaguard.navigation

sealed interface NavigationIntent {

    data class NavigateTo(val screen: Screen, val clearBackStack: Boolean) : NavigationIntent

    data object NavigateBack : NavigationIntent
}