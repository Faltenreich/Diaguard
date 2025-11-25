package com.faltenreich.diaguard.data.preference.color

import com.faltenreich.diaguard.data.preference.Preference
import com.faltenreich.diaguard.data.preference.color.ColorScheme.SYSTEM
import com.faltenreich.diaguard.data.preference.color.ColorScheme.entries
import diaguard.data.generated.resources.Res
import diaguard.data.generated.resources.preference_theme

data object ColorSchemePreference :
    Preference<Int, ColorScheme> {

    override val key = Res.string.preference_theme

    override val default = SYSTEM

    override val onRead = { stableId: Int -> entries.firstOrNull { it.stableId == stableId } }

    override val onWrite = ColorScheme::stableId
}