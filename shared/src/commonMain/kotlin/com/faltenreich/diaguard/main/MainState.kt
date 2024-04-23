package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.navigation.screen.Screen

sealed interface MainState {

    data object Loading : MainState

    data class Loaded(val startScreen: Screen) : MainState
}