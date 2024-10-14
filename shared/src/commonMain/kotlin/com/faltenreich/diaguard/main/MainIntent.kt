package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.navigation.screen.Screen

sealed interface MainIntent {

    data class OpenBottomSheet(val screen: Screen) : MainIntent

    data object CloseBottomSheet : MainIntent
}