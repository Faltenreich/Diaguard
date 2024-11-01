package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen

sealed interface MainState {

    data object FirstStart : MainState

    data class SubsequentStart(
        val startScreen: Screen,
        val topAppBarStyle: TopAppBarStyle,
        val bottomAppBarStyle: BottomAppBarStyle,
        val bottomSheet: Screen?,
    ) : MainState
}