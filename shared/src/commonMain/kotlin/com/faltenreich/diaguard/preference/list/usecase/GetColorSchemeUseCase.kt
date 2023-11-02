package com.faltenreich.diaguard.preference.list.usecase

import com.faltenreich.diaguard.preference.PreferenceStore
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow

class GetColorSchemeUseCase(
    private val preferenceStore: PreferenceStore = inject(),
) {

    operator fun invoke(): Flow<ColorScheme> {
        return preferenceStore.colorScheme
    }
}