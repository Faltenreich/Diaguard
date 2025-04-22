package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen

data class MainState(
    val startScreen: Screen,
    val topAppBarStyle: TopAppBarStyle,
    val bottomAppBarStyle: BottomAppBarStyle,
)