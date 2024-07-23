package com.faltenreich.diaguard.navigation

class NavigateToScreenUseCase(
    private val navigation: Navigation,
) {

    suspend operator fun invoke(screen: Screen) {
        navigation.push(screen)
    }
}