package com.faltenreich.diaguard.main

sealed interface MainIntent {

    data class TintStatusBars(val isAppearanceLightStatusBars: Boolean) : MainIntent

    data class PushScreen(val screen: com.faltenreich.diaguard.navigation.screen.Screen, val popHistory: Boolean) : MainIntent

    data object PopScreen : MainIntent
}