package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.data.navigation.Navigation
import com.faltenreich.diaguard.data.navigation.NavigationEvent
import com.faltenreich.diaguard.data.navigation.NavigationTarget

class NavigateToUseCase internal constructor(private val navigation: Navigation) {

    suspend operator fun invoke(target: NavigationTarget, popHistory: Boolean = false) {
        navigation.postEvent(NavigationEvent.NavigateTo(target, popHistory))
    }
}