package com.faltenreich.diaguard.preference.food

import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.combine

class FoodPreferenceListViewModel(
    getPreference: GetPreferenceUseCase,
) : ViewModel<FoodPreferenceListState, Unit, Unit>() {

    override val state = combine(
        getPreference(ShowCustomFoodPreference),
        getPreference(ShowCommonFoodPreference),
        getPreference(ShowBrandedFoodPreference),
        ::FoodPreferenceListState,
    )
}