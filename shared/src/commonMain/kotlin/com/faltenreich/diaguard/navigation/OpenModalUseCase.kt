package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.screen.Screen

class OpenModalUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke(modal: Screen) {
        navigation.pushModal(modal)
    }
}