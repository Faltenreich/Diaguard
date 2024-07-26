package com.faltenreich.diaguard.navigation.modal

import com.faltenreich.diaguard.navigation.Navigation
import kotlinx.coroutines.flow.Flow

class GetModalUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke(): Flow<Modal?> {
        return navigation.modal
    }
}