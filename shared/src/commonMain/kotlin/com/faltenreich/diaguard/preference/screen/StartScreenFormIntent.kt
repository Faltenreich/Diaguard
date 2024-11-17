package com.faltenreich.diaguard.preference.screen

sealed interface StartScreenFormIntent {

    data class Select(val startScreen: StartScreen) : StartScreenFormIntent
}