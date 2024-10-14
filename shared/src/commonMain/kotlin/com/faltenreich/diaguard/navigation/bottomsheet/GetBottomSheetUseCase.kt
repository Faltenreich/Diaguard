package com.faltenreich.diaguard.navigation.bottomsheet

import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.screen.Screen
import kotlinx.coroutines.flow.Flow

class GetBottomSheetUseCase(private val navigation: Navigation) {

    operator fun invoke(): Flow<Screen?> {
        return navigation.bottomSheet
    }
}