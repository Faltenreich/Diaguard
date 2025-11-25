package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.data.preference.color.ColorScheme

data class MainState(
    val startScreen: Screen,
    val colorScheme: ColorScheme,
    val topAppBarStyle: TopAppBarStyle,
    val bottomAppBarStyle: BottomAppBarStyle,
)