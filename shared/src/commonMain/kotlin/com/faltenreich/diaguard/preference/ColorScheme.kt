package com.faltenreich.diaguard.preference

import diaguard.shared.generated.resources.*
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
    ;

    data object Preference : com.faltenreich.diaguard.preference.Preference<Int, ColorScheme>(
        key = Res.string.preference_theme,
        default = SYSTEM,
        onRead = { stableId -> entries.firstOrNull { it.stableId == stableId } },
        onWrite = ColorScheme::stableId,
    )
}