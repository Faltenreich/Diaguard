package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.inject

class GetActiveScreenUseCase(
    private val navigation: Navigation = inject(),
) {

    operator fun invoke(): Screen? {
        return navigation.lastItem
    }
}