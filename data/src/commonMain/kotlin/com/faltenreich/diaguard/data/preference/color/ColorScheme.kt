package com.faltenreich.diaguard.data.preference.color

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

enum class ColorScheme(val stableId: Int) {

    SYSTEM(stableId = 0),
    LIGHT(stableId = 1),
    DARK(stableId = 2),
}

@Composable
fun ColorScheme?.isDark(): Boolean {
    return when (this) {
        ColorScheme.SYSTEM, null -> isSystemInDarkTheme()
        ColorScheme.LIGHT -> false
        ColorScheme.DARK -> true
    }
}