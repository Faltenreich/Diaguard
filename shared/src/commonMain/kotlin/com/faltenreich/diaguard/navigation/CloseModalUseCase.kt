package com.faltenreich.diaguard.navigation

class CloseModalUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke() {
        navigation.popModal()
    }
}