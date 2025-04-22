package com.faltenreich.diaguard

import com.faltenreich.diaguard.preference.color.ColorSchemePreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.map

class AppViewModel(getPreference: GetPreferenceUseCase) : ViewModel<AppState, Unit, Unit>() {

    override val state = getPreference(ColorSchemePreference).map(::AppState)
}