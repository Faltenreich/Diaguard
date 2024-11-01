package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.screen.Screen

sealed interface NavigationEvent {

    data class PushScreen(val screen: Screen, val popHistory: Boolean = false) : NavigationEvent

    data class PopScreen(val result: Pair<String, Any>?) : NavigationEvent
}