package com.faltenreich.diaguard

import com.faltenreich.diaguard.preference.color.ColorScheme

sealed interface AppState {

    val colorScheme: ColorScheme?

    data class FirstStart(override val colorScheme: ColorScheme?) : AppState

    data class SubsequentStart(override val colorScheme: ColorScheme?) : AppState
}