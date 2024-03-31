package com.faltenreich.diaguard.preference.store

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

    companion object {

        fun valueOf(stableId: Int): ColorScheme? {
            return entries.firstOrNull { it.stableId == stableId }
        }
    }
}