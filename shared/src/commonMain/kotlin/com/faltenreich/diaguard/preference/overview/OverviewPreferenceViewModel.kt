package com.faltenreich.diaguard.preference.overview

import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.map

class OverviewPreferenceViewModel(
    getPreferences: GetOverviewPreferencesUseCase,
) : ViewModel<OverviewPreferenceState, Unit, Unit>() {

    override val state = getPreferences().map(::OverviewPreferenceState)
}