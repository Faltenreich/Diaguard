package com.faltenreich.diaguard.navigation.bar.bottom

import com.faltenreich.diaguard.data.navigation.BottomAppBarStyle
import com.faltenreich.diaguard.data.navigation.Navigation
import kotlinx.coroutines.flow.Flow

class GetBottomAppBarStyleUseCase internal constructor(private val navigation: Navigation) {

    operator fun invoke(): Flow<BottomAppBarStyle> {
        return navigation.bottomAppBarStyle
    }
}