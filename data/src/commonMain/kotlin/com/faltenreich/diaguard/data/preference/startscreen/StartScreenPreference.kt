package com.faltenreich.diaguard.data.preference.startscreen

import com.faltenreich.diaguard.data.preference.Preference
import com.faltenreich.diaguard.data.preference.startscreen.StartScreen.DASHBOARD
import com.faltenreich.diaguard.data.preference.startscreen.StartScreen.entries

data object StartScreenPreference : Preference<Int, StartScreen> {

    override val key = "preference_start_screen"

    override val default = DASHBOARD

    override val onRead = { stableId: Int -> entries.firstOrNull { it.stableId == stableId } }

    override val onWrite = StartScreen::stableId
}