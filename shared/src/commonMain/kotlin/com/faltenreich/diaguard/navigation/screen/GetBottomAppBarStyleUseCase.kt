package com.faltenreich.diaguard.navigation.screen

import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import kotlinx.coroutines.flow.Flow

class GetBottomAppBarStyleUseCase(private val navigation: Navigation) {

    operator fun invoke(): Flow<BottomAppBarStyle> {
        return navigation.bottomAppBarStyle
    }
}