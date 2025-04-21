package com.faltenreich.diaguard.preference.food

import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.preference.store.SetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.combine

class FoodPreferenceListViewModel(
    getPreference: GetPreferenceUseCase,
    private val setPreference: SetPreferenceUseCase,
) : ViewModel<FoodPreferenceListState, FoodPreferenceListIntent, Unit>() {

    override val state = combine(
        getPreference(ShowCustomFoodPreference),
        getPreference(ShowCommonFoodPreference),
        getPreference(ShowBrandedFoodPreference),
        ::FoodPreferenceListState,
    )

    override suspend fun handleIntent(intent: FoodPreferenceListIntent) {
        when (intent) {
            is FoodPreferenceListIntent.SetShowCustomFood ->
                setPreference(ShowCustomFoodPreference, intent.showCustomFood)
            is FoodPreferenceListIntent.SetShowCommonFood ->
                setPreference(ShowCommonFoodPreference, intent.showCommonFood)
            is FoodPreferenceListIntent.SetShowBrandedFood ->
                setPreference(ShowBrandedFoodPreference, intent.showBrandedFood)
        }
    }
}