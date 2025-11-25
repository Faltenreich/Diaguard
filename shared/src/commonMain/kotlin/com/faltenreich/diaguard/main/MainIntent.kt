package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.navigation.NavigationTarget

sealed interface MainIntent {

    data class TintStatusBars(val isAppearanceLightStatusBars: Boolean) : MainIntent

    data class NavigateTo(val target: NavigationTarget, val popHistory: Boolean) : MainIntent

    data object PopScreen : MainIntent
}