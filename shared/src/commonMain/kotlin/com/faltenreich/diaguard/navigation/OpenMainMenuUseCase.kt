package com.faltenreich.diaguard.navigation

class OpenMainMenuUseCase(private val navigation: Navigation) {

    suspend operator fun invoke() {
        navigation.postEvent(NavigationEvent.OpenMainMenu)
    }
}