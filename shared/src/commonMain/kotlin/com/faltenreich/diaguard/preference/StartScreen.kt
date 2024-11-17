package com.faltenreich.diaguard.preference

import diaguard.shared.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class StartScreen(
    val stableId: Int,
    val labelResource: StringResource,
    val iconResource: DrawableResource,
) {

    DASHBOARD(
        stableId = 0,
        labelResource = Res.string.dashboard,
        iconResource = Res.drawable.ic_dashboard,
    ),
    TIMELINE(
        stableId = 1,
        labelResource = Res.string.timeline,
        iconResource = Res.drawable.ic_timeline,
    ),
    LOG(
        stableId = 2,
        labelResource = Res.string.log,
        iconResource = Res.drawable.ic_log,
    ),
    ;

    data object Preference : com.faltenreich.diaguard.preference.Preference<Int, StartScreen>(
        key = Res.string.preference_start_screen,
        default = DASHBOARD,
        onRead = { stableId -> entries.firstOrNull { it.stableId == stableId } },
        onWrite = StartScreen::stableId,
    )
}