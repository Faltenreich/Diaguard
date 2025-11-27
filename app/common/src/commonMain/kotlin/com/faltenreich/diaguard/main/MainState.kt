package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.data.navigation.Screen
import com.faltenreich.diaguard.data.preference.color.ColorScheme
import com.faltenreich.diaguard.view.bar.BottomAppBarStyle
import com.faltenreich.diaguard.view.bar.TopAppBarStyle

data class MainState(
    val startScreen: Screen,
    val colorScheme: ColorScheme,
    val topAppBarStyle: TopAppBarStyle,
    val bottomAppBarStyle: BottomAppBarStyle,
)