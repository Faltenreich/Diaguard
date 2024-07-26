package com.faltenreich.diaguard.navigation

import kotlinx.coroutines.flow.Flow

class GetCurrentScreenUseCase(private val navigation: Navigation) {

    operator fun invoke(): Flow<Screen?> {
        return navigation.currentScreen
    }
}