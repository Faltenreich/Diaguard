package com.faltenreich.diaguard.data.preference.color

import com.faltenreich.diaguard.data.preference.Preference
import com.faltenreich.diaguard.data.preference.color.ColorScheme.SYSTEM
import com.faltenreich.diaguard.data.preference.color.ColorScheme.entries

data object ColorSchemePreference : Preference<Int, ColorScheme> {

    override val key = "theme"

    override val default = SYSTEM

    override val onRead = { stableId: Int -> entries.firstOrNull { it.stableId == stableId } }

    override val onWrite = ColorScheme::stableId
}