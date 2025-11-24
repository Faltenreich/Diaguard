package com.faltenreich.diaguard.navigation.screen

import kotlinx.coroutines.flow.Flow

class GetBottomAppBarStyleUseCase(private val navigation: com.faltenreich.diaguard.navigation.Navigation) {

    operator fun invoke(): Flow<com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle> {
        return navigation.bottomAppBarStyle
    }
}