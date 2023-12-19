package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.screen.Screen

class NavigateToUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke(screen: Screen) {
        navigation.push(screen)
    }
}