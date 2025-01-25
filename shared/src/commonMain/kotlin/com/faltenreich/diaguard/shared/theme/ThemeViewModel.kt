package com.faltenreich.diaguard.shared.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.preference.color.ColorScheme
import com.faltenreich.diaguard.preference.color.ColorSchemePreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel

class ThemeViewModel(getPreference: GetPreferenceUseCase) : ViewModel<ColorScheme, Unit, Unit>() {

    override val state = getPreference(ColorSchemePreference)

    @Composable
    fun isDarkColorScheme(): Boolean {
        return when (collectState()) {
            ColorScheme.SYSTEM, null -> isSystemInDarkTheme()
            ColorScheme.LIGHT -> false
            ColorScheme.DARK -> true
        }
    }
}