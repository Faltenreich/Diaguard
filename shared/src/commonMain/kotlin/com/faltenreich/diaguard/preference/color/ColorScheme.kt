package com.faltenreich.diaguard.preference.color

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.color_scheme_dark
import diaguard.shared.generated.resources.color_scheme_light
import diaguard.shared.generated.resources.color_scheme_system
import org.jetbrains.compose.resources.StringResource

enum class ColorScheme(
    val stableId: Int,
    val labelResource: StringResource,
) {
    SYSTEM(
        stableId = 0,
        labelResource = Res.string.color_scheme_system,
    ),
    LIGHT(
        stableId = 1,
        labelResource = Res.string.color_scheme_light,
    ),
    DARK(
        stableId = 2,
        labelResource = Res.string.color_scheme_dark,
    ),
}

@Composable
fun ColorScheme?.isDark(): Boolean {
    return when (this) {
        ColorScheme.SYSTEM, null -> isSystemInDarkTheme()
        ColorScheme.LIGHT -> false
        ColorScheme.DARK -> true
    }
}