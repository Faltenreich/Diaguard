package com.faltenreich.diaguard.navigation.bottomsheet

import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.navigation.screen.Screen

class OpenBottomSheetUseCase(private val navigation: Navigation) {

    suspend operator fun invoke(bottomSheet: Screen) {
        navigation.postEvent(NavigationEvent.OpenBottomSheet(bottomSheet))
    }
}