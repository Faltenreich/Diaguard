package com.faltenreich.diaguard.navigation.screen

import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.navigation.NavigationTarget

class NavigateToUseCase(private val navigation: Navigation) {

    suspend operator fun invoke(target: NavigationTarget, popHistory: Boolean = false) {
        navigation.postEvent(NavigationEvent.NavigateTo(target, popHistory))
    }
}