package com.faltenreich.diaguard.main.menu

import com.faltenreich.diaguard.navigation.screen.Screen

sealed interface MainMenuIntent {

    data class NavigateTo(val screen: Screen, val popHistory: Boolean) : MainMenuIntent
}