package com.faltenreich.diaguard.preference.list.usecase

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
    TIME_OF_DAY(
        stableId = 1,
        labelResource = MR.strings.color_scheme_time_of_day,
    ),
    LIGHT(
        stableId = 2,
        labelResource = MR.strings.color_scheme_light,
    ),
    DARK(
        stableId = 3,
        labelResource = MR.strings.color_scheme_dark,
    ),
    ;

    companion object {

        fun valueOf(stableId: Int): ColorScheme {
            return ColorScheme.values().first { it.stableId == stableId }
        }
    }
}