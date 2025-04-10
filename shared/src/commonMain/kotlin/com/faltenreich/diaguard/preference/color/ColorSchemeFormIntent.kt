package com.faltenreich.diaguard.preference.color

sealed interface ColorSchemeFormIntent {

    data class Select(val colorScheme: ColorScheme) : ColorSchemeFormIntent
}