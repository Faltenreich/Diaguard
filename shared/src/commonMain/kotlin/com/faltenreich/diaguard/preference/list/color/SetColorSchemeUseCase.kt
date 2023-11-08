package com.faltenreich.diaguard.preference.list.color

import com.faltenreich.diaguard.preference.PreferenceStore
import com.faltenreich.diaguard.shared.di.inject

class SetColorSchemeUseCase(
    private val preferenceStore: PreferenceStore = inject(),
) {

    suspend operator fun invoke(colorScheme: ColorScheme) {
        preferenceStore.setColorScheme(colorScheme)
    }
}