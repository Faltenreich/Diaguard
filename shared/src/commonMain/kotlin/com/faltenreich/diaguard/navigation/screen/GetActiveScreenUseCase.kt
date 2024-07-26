package com.faltenreich.diaguard.navigation.screen

import com.faltenreich.diaguard.navigation.Navigation

class GetActiveScreenUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke(): Screen? {
        return navigation.currentScreen.value
    }
}