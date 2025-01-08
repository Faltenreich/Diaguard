package com.faltenreich.diaguard.preference.food

import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.combine

class FoodPreferenceListViewModel(
    getPreferences: GetFoodPreferencesUseCase,
    showAnyFood: ShowAnyFoodUseCase,
) : ViewModel<FoodPreferenceListState, Unit, Unit>() {

    override val state = combine(
        getPreferences(),
        showAnyFood(),
        ::FoodPreferenceListState,
    )
}