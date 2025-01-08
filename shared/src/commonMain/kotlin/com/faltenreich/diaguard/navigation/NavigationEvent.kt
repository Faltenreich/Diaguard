package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.modal.Modal
import com.faltenreich.diaguard.navigation.screen.Screen

sealed interface NavigationEvent {

    data class PushScreen(val screen: Screen, val popHistory: Boolean) : NavigationEvent

    data object PopScreen : NavigationEvent

    data class OpenBottomSheet(val bottomSheet: Screen) : NavigationEvent

    data object CloseBottomSheet : NavigationEvent

    data class OpenModal(val modal: Modal) : NavigationEvent

    data object CloseModal : NavigationEvent
}