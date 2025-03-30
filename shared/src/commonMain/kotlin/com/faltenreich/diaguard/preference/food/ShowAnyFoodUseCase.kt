package com.faltenreich.diaguard.preference.food

import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ShowAnyFoodUseCase(
    private val getPreference: GetPreferenceUseCase,
) {

    operator fun invoke(): Flow<Boolean> {
        return combine(
            getPreference(ShowBrandedFoodPreference),
            getPreference(ShowCommonFoodPreference),
            getPreference(ShowCustomFoodPreference),
        ) { showBrandedFood, showCommonFood, showCustomFood ->
            showBrandedFood || showCommonFood || showCustomFood
        }
    }
}