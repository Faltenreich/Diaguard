package com.faltenreich.diaguard.preference.screen

import com.faltenreich.diaguard.preference.Preference
import com.faltenreich.diaguard.preference.screen.StartScreen.DASHBOARD
import com.faltenreich.diaguard.preference.screen.StartScreen.entries
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.preference_start_screen

data object StartScreenPreference : Preference<Int, StartScreen> {

    override val key = Res.string.preference_start_screen

    override val default = DASHBOARD

    override val onRead = { stableId: Int -> entries.firstOrNull { it.stableId == stableId } }

    override val onWrite = StartScreen::stableId
}