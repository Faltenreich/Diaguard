package com.faltenreich.diaguard

import com.faltenreich.diaguard.preference.color.ColorSchemePreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.architecture.ViewModel
import com.faltenreich.diaguard.startup.HasDataUseCase
import kotlinx.coroutines.flow.combine

class AppViewModel(
    hasData: HasDataUseCase,
    getPreference: GetPreferenceUseCase,
) : ViewModel<AppState, Unit, Unit>() {

    override val state = combine(
        hasData(),
        getPreference(ColorSchemePreference),
    ) { hasData, colorScheme ->
        if (hasData) {
            AppState.SubsequentStart(colorScheme = colorScheme)
        } else {
            AppState.FirstStart(colorScheme = null)
        }
    }
}