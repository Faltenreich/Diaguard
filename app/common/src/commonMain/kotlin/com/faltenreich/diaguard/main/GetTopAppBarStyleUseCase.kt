package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.data.navigation.Navigation
import com.faltenreich.diaguard.view.bar.TopAppBarStyle
import kotlinx.coroutines.flow.Flow

class GetTopAppBarStyleUseCase internal constructor(private val navigation: Navigation) {

    operator fun invoke(): Flow<TopAppBarStyle> {
        return navigation.topAppBarStyle
    }
}