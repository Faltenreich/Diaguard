package com.faltenreich.diaguard.preference

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

    data object Preference : com.faltenreich.diaguard.preference.Preference<Int, StartScreen>(
        key = MR.strings.preference_start_screen,
        default = DASHBOARD,
        onRead = { stableId -> entries.firstOrNull { it.stableId == stableId } },
        onWrite = StartScreen::stableId,
    )
}