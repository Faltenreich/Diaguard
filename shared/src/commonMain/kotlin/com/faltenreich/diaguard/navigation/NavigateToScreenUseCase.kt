package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.screen.Screen

class NavigateToScreenUseCase(
    private val navigation: Navigation,
) {

    suspend operator fun invoke(screen: Screen) {
        navigation.push(screen)
    }
}