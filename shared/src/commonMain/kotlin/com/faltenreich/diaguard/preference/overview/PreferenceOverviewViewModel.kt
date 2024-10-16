package com.faltenreich.diaguard.preference.overview

import com.faltenreich.diaguard.preference.list.item.PreferenceListItem
import com.faltenreich.diaguard.shared.architecture.ViewModel

class PreferenceOverviewViewModel(
    getDefaultPreferences: GetOverviewPreferencesUseCase,
) : ViewModel<List<PreferenceListItem>, Unit, Unit>() {

    override val state = getDefaultPreferences()
}