package com.faltenreich.diaguard.navigation

class GetActiveScreenUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke(): Screen? {
        return navigation.currentScreen.value
    }
}