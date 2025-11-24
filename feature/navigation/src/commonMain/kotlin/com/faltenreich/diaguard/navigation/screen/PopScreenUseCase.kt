package com.faltenreich.diaguard.navigation.screen

import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent

class PopScreenUseCase(private val navigation: Navigation) {

    suspend operator fun invoke() {
        navigation.postEvent(NavigationEvent.PopScreen)
    }
}