package com.faltenreich.diaguard.preference.list.item.color

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.preference.list.item.SelectablePreference
import dev.icerock.moko.resources.StringResource

enum class ColorScheme(
    override val stableId: Int,
    override val labelResource: StringResource,
) : SelectablePreference {
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
            return ColorScheme.values().firstOrNull { it.stableId == stableId }
        }
    }
}