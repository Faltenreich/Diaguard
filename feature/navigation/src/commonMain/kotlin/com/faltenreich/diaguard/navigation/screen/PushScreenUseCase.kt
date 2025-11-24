package com.faltenreich.diaguard.navigation.screen

class PushScreenUseCase(private val navigation: com.faltenreich.diaguard.navigation.Navigation) {

    suspend operator fun invoke(screen: com.faltenreich.diaguard.navigation.screen.Screen, popHistory: Boolean = false) {
        navigation.postEvent(_root_ide_package_.com.faltenreich.diaguard.navigation.NavigationEvent.PushScreen(screen, popHistory))
    }
}