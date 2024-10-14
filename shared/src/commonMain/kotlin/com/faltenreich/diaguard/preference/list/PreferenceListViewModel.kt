package com.faltenreich.diaguard.preference.list

import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.map

class PreferenceListViewModel(
    getDefaultPreferences: GetDefaultPreferencesUseCase,
) : ViewModel<PreferenceListState, Unit, Unit>() {

    override val state = getDefaultPreferences().map(::PreferenceListState)
}