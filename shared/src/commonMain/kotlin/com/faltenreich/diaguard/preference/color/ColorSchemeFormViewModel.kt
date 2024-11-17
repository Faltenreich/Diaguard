package com.faltenreich.diaguard.preference.color

import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.preference.store.SetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.map

class ColorSchemeFormViewModel(
    getPreference: GetPreferenceUseCase,
    private val setPreference: SetPreferenceUseCase,
) : ViewModel<ColorSchemeFormState, ColorSchemeFormIntent, Unit>() {

    override val state = getPreference(ColorSchemePreference).map(::ColorSchemeFormState)

    override suspend fun handleIntent(intent: ColorSchemeFormIntent) = with(intent) {
        when (this) {
            is ColorSchemeFormIntent.Select -> setPreference(ColorSchemePreference, colorScheme)
        }
    }
}