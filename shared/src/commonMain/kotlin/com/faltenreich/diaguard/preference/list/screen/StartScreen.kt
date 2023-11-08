package com.faltenreich.diaguard.preference.list.screen

import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.StringResource

enum class StartScreen(
    val stableId: Int,
    val labelResource: StringResource,
) {
    DASHBOARD(
        stableId = 0,
        labelResource = MR.strings.dashboard,
    ),
    TIMELINE(
        stableId = 1,
        labelResource = MR.strings.timeline,
    ),
    LOG(
        stableId = 2,
        labelResource = MR.strings.log,
    ),
    ;

    companion object {

        fun valueOf(stableId: Int): StartScreen? {
            return values().firstOrNull { it.stableId == stableId }
        }
    }
}