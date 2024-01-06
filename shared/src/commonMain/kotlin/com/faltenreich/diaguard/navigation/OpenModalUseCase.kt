package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.modal.Modal

class OpenModalUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke(modal: Modal) {
        navigation.pushModal(modal)
    }
}