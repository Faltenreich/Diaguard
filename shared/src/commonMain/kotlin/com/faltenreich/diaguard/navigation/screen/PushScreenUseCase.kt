package com.faltenreich.diaguard.navigation.screen

import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent

class PushScreenUseCase(private val navigation: Navigation) {

    suspend operator fun invoke(screen: Screen, popHistory: Boolean = false) {
        navigation.postEvent(NavigationEvent.PushScreen(screen, popHistory))
    }
}