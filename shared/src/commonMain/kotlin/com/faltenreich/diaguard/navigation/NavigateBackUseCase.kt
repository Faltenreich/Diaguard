package com.faltenreich.diaguard.navigation

class NavigateBackUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke() {
        navigation.pop()
    }
}