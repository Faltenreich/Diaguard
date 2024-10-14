package com.faltenreich.diaguard.navigation.screen

import com.faltenreich.diaguard.navigation.Navigation

class NavigateToScreenUseCase(
    private val navigation: Navigation,
) {

    suspend operator fun invoke(screen: Screen, popHistory: Boolean = false) {
        navigation.pushScreen(screen, popHistory)
    }
}