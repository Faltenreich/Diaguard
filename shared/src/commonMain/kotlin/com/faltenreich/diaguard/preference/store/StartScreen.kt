package com.faltenreich.diaguard.preference.store

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
            return entries.firstOrNull { it.stableId == stableId }
        }
    }
}