package com.faltenreich.diaguard.navigation.screen

import com.faltenreich.diaguard.navigation.Navigation
import kotlinx.coroutines.flow.Flow

class GetActiveScreenUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke(): Flow<Screen?> {
        return navigation.currentScreen
    }
}