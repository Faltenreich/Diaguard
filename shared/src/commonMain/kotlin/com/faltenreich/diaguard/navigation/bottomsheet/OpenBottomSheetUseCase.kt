package com.faltenreich.diaguard.navigation.bottomsheet

import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.screen.Screen

class OpenBottomSheetUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke(screen: Screen) {
        navigation.pushBottomSheet(screen)
    }
}