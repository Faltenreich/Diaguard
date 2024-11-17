package com.faltenreich.diaguard.preference.screen

import com.faltenreich.diaguard.preference.StartScreen

sealed interface StartScreenFormIntent {

    data class Select(val startScreen: StartScreen) : StartScreenFormIntent
}