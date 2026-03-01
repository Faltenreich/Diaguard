package com.faltenreich.diaguard.data.preference.startscreen

import com.faltenreich.diaguard.data.preference.Preference

data object StartScreenPreference : Preference<Int, StartScreen> {

    override val key = "preference_start_screen"

    override val default = StartScreen.DASHBOARD

    override val onRead = { stableId: Int -> StartScreen.entries.firstOrNull { it.stableId == stableId } }

    override val onWrite = StartScreen::stableId
}