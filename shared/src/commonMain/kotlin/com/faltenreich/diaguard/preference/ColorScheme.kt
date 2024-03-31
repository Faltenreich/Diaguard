package com.faltenreich.diaguard.preference

import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.StringResource

enum class ColorScheme(
    val stableId: Int,
    val labelResource: StringResource,
) {
    SYSTEM(
        stableId = 0,
        labelResource = MR.strings.color_scheme_system,
    ),
    LIGHT(
        stableId = 1,
        labelResource = MR.strings.color_scheme_light,
    ),
    DARK(
        stableId = 2,
        labelResource = MR.strings.color_scheme_dark,
    ),
    ;

    data object Preference : com.faltenreich.diaguard.preference.Preference<Int, ColorScheme>(
        key = MR.strings.preference_theme,
        default = SYSTEM,
        onRead = { stableId -> entries.firstOrNull { it.stableId == stableId } },
        onWrite = ColorScheme::stableId,
    )
}