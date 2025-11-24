package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.preference.color.ColorScheme

data class MainState(
    val startScreen: com.faltenreich.diaguard.navigation.screen.Screen,
    val colorScheme: ColorScheme,
    val topAppBarStyle: com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle,
    val bottomAppBarStyle: com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle,
)