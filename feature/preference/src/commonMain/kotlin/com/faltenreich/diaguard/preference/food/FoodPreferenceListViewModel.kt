package com.faltenreich.diaguard.preference.food

import com.faltenreich.diaguard.system.web.OpenUrlUseCase
import com.faltenreich.diaguard.preference.GetPreferenceUseCase
import com.faltenreich.diaguard.preference.SetPreferenceUseCase
import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import com.faltenreich.diaguard.data.preference.food.ShowBrandedFoodPreference
import com.faltenreich.diaguard.data.preference.food.ShowCommonFoodPreference
import com.faltenreich.diaguard.data.preference.food.ShowCustomFoodPreference
import kotlinx.coroutines.flow.combine

internal class FoodPreferenceListViewModel(
    getPreference: GetPreferenceUseCase,
    private val setPreference: SetPreferenceUseCase,
    private val openUrl: OpenUrlUseCase,
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
            is FoodPreferenceListIntent.OpenUrl ->
                openUrl(intent.url)
        }
    }
}