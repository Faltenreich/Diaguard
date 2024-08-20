package com.faltenreich.diaguard.navigation.screen

import com.faltenreich.diaguard.navigation.Navigation

class NavigateBackUseCase(
    private val navigation: Navigation,
) {

    suspend operator fun invoke() {
        navigation.popScreen()
    }
}