package com.faltenreich.diaguard.preference.list

import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.map

class PreferenceListViewModel(
    getDefaultPreferences: GetDefaultPreferencesUseCase = inject(),
) : ViewModel<PreferenceListState, Unit, Unit>() {

    override val state = getDefaultPreferences().map(::PreferenceListState)
}