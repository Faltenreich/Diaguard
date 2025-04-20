package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.navigation.screen.Screen

sealed interface MainIntent {

    data class PushScreen(val screen: Screen, val popHistory: Boolean) : MainIntent

    data object PopScreen : MainIntent

    data class OpenBottomSheet(val screen: Screen) : MainIntent

    data object CloseBottomSheet : MainIntent
}