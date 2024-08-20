package com.faltenreich.diaguard.navigation.modal

import com.faltenreich.diaguard.navigation.Navigation

class OpenModalUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke(modal: Modal) {
        navigation.openModal(modal)
    }
}