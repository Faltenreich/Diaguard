package com.faltenreich.diaguard.navigation.modal

import com.faltenreich.diaguard.navigation.Navigation

class CloseModalUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke() {
        navigation.closeModal()
    }
}