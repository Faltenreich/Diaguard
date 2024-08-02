package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.navigation.modal.Modal
import com.faltenreich.diaguard.navigation.screen.Screen

sealed interface MainState {

    data object FirstStart : MainState

    data class SubsequentStart(
        val startScreen: Screen,
        val currentScreen: Screen?,
        val bottomSheet: Screen?,
        val modal: Modal?,
    ) : MainState
}