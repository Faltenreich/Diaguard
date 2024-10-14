package com.faltenreich.diaguard.navigation.screen

import com.faltenreich.diaguard.navigation.Navigation

class CanNavigateBackUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke(): Boolean {
        return navigation.canPopScreen()
    }
}