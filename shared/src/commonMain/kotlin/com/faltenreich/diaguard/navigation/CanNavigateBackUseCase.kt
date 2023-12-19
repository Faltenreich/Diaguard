package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.shared.di.inject

class CanNavigateBackUseCase(
    private val navigation: Navigation = inject(),
) {

    operator fun invoke(): Boolean {
        return navigation.canPop()
    }
}