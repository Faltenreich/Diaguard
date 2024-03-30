package com.faltenreich.diaguard.preference.store.color

import com.faltenreich.diaguard.preference.Preference
import com.faltenreich.diaguard.preference.store.PreferenceStore
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetColorSchemeUseCase(
    private val preferenceStore: PreferenceStore = inject(),
) {

    operator fun invoke(): Flow<ColorScheme> {
        return preferenceStore.read(Preference.ColorScheme).map { stableId ->
            stableId?.let(ColorScheme::valueOf) ?: ColorScheme.SYSTEM
        }
    }
}