package com.faltenreich.diaguard.navigation.modal

import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent

class CloseModalUseCase(private val navigation: Navigation) {

    suspend operator fun invoke() {
        navigation.postEvent(NavigationEvent.CloseModal)
    }
}