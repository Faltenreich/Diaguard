package com.faltenreich.diaguard.navigation

class CanNavigateBackUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke(): Boolean {
        return navigation.canPop()
    }
}