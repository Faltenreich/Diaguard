package com.faltenreich.diaguard.preference.store.color

import com.faltenreich.diaguard.preference.store.PreferenceStore
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow

class GetColorSchemeUseCase(
    private val preferenceStore: PreferenceStore = inject(),
) {

    operator fun invoke(): Flow<ColorScheme> {
        return preferenceStore.colorScheme
    }
}