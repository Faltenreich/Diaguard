package com.faltenreich.diaguard.navigation.screen

import kotlinx.coroutines.flow.Flow

class GetTopAppBarStyleUseCase(private val navigation: com.faltenreich.diaguard.navigation.Navigation) {

    operator fun invoke(): Flow<com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle> {
        return navigation.topAppBarStyle
    }
}