package com.faltenreich.diaguard

import com.faltenreich.diaguard.preference.color.ColorSchemePreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.startup.HasDataUseCase
import com.faltenreich.diaguard.startup.MigrateDataUseCase
import kotlinx.coroutines.flow.combine

class AppViewModel(
    hasData: HasDataUseCase,
    getPreference: GetPreferenceUseCase,
    private val migrateData: MigrateDataUseCase,
) : ViewModel<AppState, AppIntent, Unit>() {

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

    override suspend fun handleIntent(intent: AppIntent) {
        when (intent) {
            is AppIntent.MigrateData -> migrateData()
        }
    }
}