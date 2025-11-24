package com.faltenreich.diaguard.navigation.screen

class PopScreenUseCase(private val navigation: com.faltenreich.diaguard.navigation.Navigation) {

    suspend operator fun invoke() {
        navigation.postEvent(_root_ide_package_.com.faltenreich.diaguard.navigation.NavigationEvent.PopScreen)
    }
}