package com.faltenreich.diaguard.navigation.bottomsheet

import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent

class CloseBottomSheetUseCase(private val navigation: Navigation) {

    suspend operator fun invoke() {
        navigation.postEvent(NavigationEvent.CloseBottomSheet)
    }
}