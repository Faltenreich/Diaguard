package com.faltenreich.diaguard.shared.theme

import com.faltenreich.diaguard.preference.ColorScheme
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel

class ThemeViewModel(getPreference: GetPreferenceUseCase) : ViewModel<ColorScheme, Unit, Unit>() {

    override val state = getPreference(ColorScheme.Preference)
}