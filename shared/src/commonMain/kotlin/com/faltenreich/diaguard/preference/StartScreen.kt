package com.faltenreich.diaguard.preference

import diaguard.shared.generated.resources.*
import org.jetbrains.compose.resources.StringResource

enum class StartScreen(
    val stableId: Int,
    val labelResource: StringResource,
) {

    DASHBOARD(
        stableId = 0,
        labelResource = Res.string.dashboard,
    ),
    TIMELINE(
        stableId = 1,
        labelResource = Res.string.timeline,
    ),
    LOG(
        stableId = 2,
        labelResource = Res.string.log,
    ),
    ;

    data object Preference : com.faltenreich.diaguard.preference.Preference<Int, StartScreen>(
        key = Res.string.preference_start_screen,
        default = DASHBOARD,
        onRead = { stableId -> entries.firstOrNull { it.stableId == stableId } },
        onWrite = StartScreen::stableId,
    )
}