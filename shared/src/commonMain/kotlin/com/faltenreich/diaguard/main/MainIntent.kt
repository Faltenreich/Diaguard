package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.navigation.screen.Screen

sealed interface MainIntent {

    data class PushScreen(val screen: Screen, val popHistory: Boolean) : MainIntent

    data object PopScreen : MainIntent
}