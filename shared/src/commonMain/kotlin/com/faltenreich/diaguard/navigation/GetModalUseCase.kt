package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.modal.Modal
import kotlinx.coroutines.flow.Flow

class GetModalUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke(): Flow<Modal?> {
        return navigation.modal
    }
}