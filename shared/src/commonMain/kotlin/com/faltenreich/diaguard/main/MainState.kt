package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.data.navigation.BottomAppBarStyle
import com.faltenreich.diaguard.data.navigation.TopAppBarStyle
import com.faltenreich.diaguard.data.navigation.Screen
import com.faltenreich.diaguard.data.preference.color.ColorScheme

data class MainState(
    val startScreen: Screen,
    val colorScheme: ColorScheme,
    val topAppBarStyle: TopAppBarStyle,
    val bottomAppBarStyle: BottomAppBarStyle,
)