package com.faltenreich.diaguard.navigation.top

import com.faltenreich.diaguard.navigation.Screen

fun Screen.topAppBarStyle(): TopAppBarStyle {
    return when (this) {
        else -> TopAppBarStyle.Hidden
    }
}