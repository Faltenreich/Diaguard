package com.faltenreich.diaguard.navigation

class NavigateBackUseCase(
    private val navigation: Navigation,
) {

    suspend operator fun invoke() {
        navigation.pop()
    }
}