package com.faltenreich.diaguard.preference.color

import com.faltenreich.diaguard.preference.color.ColorScheme.SYSTEM
import com.faltenreich.diaguard.preference.color.ColorScheme.entries
import diaguard.feature.preference.generated.resources.Res
import diaguard.feature.preference.generated.resources.preference_theme

data object ColorSchemePreference :
    com.faltenreich.diaguard.preference.Preference<Int, ColorScheme> {

    override val key = Res.string.preference_theme

    override val default = SYSTEM

    override val onRead = { stableId: Int -> entries.firstOrNull { it.stableId == stableId } }

    override val onWrite = ColorScheme::stableId
}