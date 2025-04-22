package com.faltenreich.diaguard

import com.faltenreich.diaguard.startup.HasDataUseCase
import com.faltenreich.diaguard.startup.MigrateDataUseCase
import com.faltenreich.diaguard.preference.color.ColorSchemePreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class AppViewModel(
    hasData: HasDataUseCase,
    getPreference: GetPreferenceUseCase,
    private val migrateData: MigrateDataUseCase,
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

    init {
        scope.launch { migrateData() }
    }
}