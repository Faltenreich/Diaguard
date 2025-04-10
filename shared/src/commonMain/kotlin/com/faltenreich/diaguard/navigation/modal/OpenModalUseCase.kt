package com.faltenreich.diaguard.navigation.modal

import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent

class OpenModalUseCase(private val navigation: Navigation) {

    suspend operator fun invoke(modal: Modal) {
        navigation.postEvent(NavigationEvent.OpenModal(modal))
    }
}