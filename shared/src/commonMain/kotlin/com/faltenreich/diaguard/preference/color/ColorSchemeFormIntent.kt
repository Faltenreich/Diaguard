package com.faltenreich.diaguard.preference.color

import com.faltenreich.diaguard.preference.ColorScheme

sealed interface ColorSchemeFormIntent {

    data class Select(val colorScheme: ColorScheme) : ColorSchemeFormIntent
}