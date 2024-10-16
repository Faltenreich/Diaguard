package com.faltenreich.diaguard.preference.food

import com.faltenreich.diaguard.preference.list.item.PreferenceListItem
import com.faltenreich.diaguard.shared.architecture.ViewModel

class FoodPreferenceViewModel(
    getPreferences: GetFoodPreferencesUseCase,
) : ViewModel<List<PreferenceListItem>, Unit, Unit>() {

    override val state = getPreferences()
}