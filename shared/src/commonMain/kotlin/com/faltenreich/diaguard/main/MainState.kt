package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.navigation.Modal
import com.faltenreich.diaguard.navigation.Screen

sealed interface MainState {

    data object Loading : MainState

    data class Loaded(
        val startScreen: Screen,
        val modal: Modal?,
    ) : MainState
}