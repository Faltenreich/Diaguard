package com.faltenreich.diaguard.navigation

class OpenModalUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke(modal: Modal) {
        navigation.pushModal(modal)
    }
}