package com.faltenreich.diaguard.navigation.bar.bottom

import com.faltenreich.diaguard.data.navigation.Navigation
import com.faltenreich.diaguard.view.bar.BottomAppBarStyle
import kotlinx.coroutines.flow.Flow

class GetBottomAppBarStyleUseCase internal constructor(private val navigation: Navigation) {

    operator fun invoke(): Flow<BottomAppBarStyle> {
        return navigation.bottomAppBarStyle
    }
}