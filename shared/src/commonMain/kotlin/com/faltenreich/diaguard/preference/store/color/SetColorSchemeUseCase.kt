package com.faltenreich.diaguard.preference.store.color

import com.faltenreich.diaguard.preference.Preference
import com.faltenreich.diaguard.preference.store.PreferenceStore
import com.faltenreich.diaguard.shared.di.inject

class SetColorSchemeUseCase(
    private val preferenceStore: PreferenceStore = inject(),
) {

    suspend operator fun invoke(colorScheme: ColorScheme) {
        preferenceStore.write(Preference.ColorScheme, colorScheme.stableId)
    }
}