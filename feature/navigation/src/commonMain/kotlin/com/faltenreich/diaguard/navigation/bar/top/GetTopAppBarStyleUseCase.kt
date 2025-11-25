package com.faltenreich.diaguard.navigation.bar.top

import com.faltenreich.diaguard.navigation.Navigation
import kotlinx.coroutines.flow.Flow

class GetTopAppBarStyleUseCase(private val navigation: Navigation) {

    operator fun invoke(): Flow<TopAppBarStyle> {
        return navigation.topAppBarStyle
    }
}