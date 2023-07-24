package com.faltenreich.diaguard.preference

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.preference.list.item.SelectablePreference
import dev.icerock.moko.resources.StringResource

enum class StartScreen(
    override val stableId: Int,
    val labelResource: StringResource,
) : SelectablePreference {
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

        fun valueOf(stableId: Int): StartScreen {
            return values().first { it.stableId == stableId }
        }
    }
}