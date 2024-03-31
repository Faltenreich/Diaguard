package com.faltenreich.diaguard.shared.theme

import com.faltenreich.diaguard.preference.ColorScheme
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow

class ThemeViewModel(
    getPreference: GetPreferenceUseCase = inject(),
) : ViewModel<ColorScheme, Unit>() {

    override val state: Flow<ColorScheme> = getPreference(ColorScheme.Preference)

    override fun onIntent(intent: Unit) = Unit
}