package com.faltenreich.diaguard.preference.overview

import com.faltenreich.diaguard.preference.color.ColorSchemePreference
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.screen.StartScreenPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.config.GetAppVersionUseCase
import kotlinx.coroutines.flow.combine

class OverviewPreferenceListViewModel(
    getPreferences: GetOverviewPreferencesUseCase,
    getPreference: GetPreferenceUseCase,
    getAppVersion: GetAppVersionUseCase,
) : ViewModel<OverviewPreferenceListState, Unit, Unit>() {

    override val state = combine(
        getPreferences(),
        getPreference(ColorSchemePreference),
        getPreference(StartScreenPreference),
        getPreference(DecimalPlacesPreference),
        getAppVersion(),
        ::OverviewPreferenceListState,
    )
}