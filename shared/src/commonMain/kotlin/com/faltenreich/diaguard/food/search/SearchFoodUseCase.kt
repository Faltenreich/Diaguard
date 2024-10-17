package com.faltenreich.diaguard.food.search

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.preference.FoodPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.data.PagingPage
import kotlinx.coroutines.flow.first

class SearchFoodUseCase(
    private val foodRepository: FoodRepository,
    private val getPreference: GetPreferenceUseCase,
) {

    suspend operator fun invoke(query: String, page: PagingPage): List<Food.Local> {
        val showCommonFood = getPreference(FoodPreference.ShowCommonFood).first()
        val showCustomFood = getPreference(FoodPreference.ShowCustomFood).first()
        val showBrandedFood = getPreference(FoodPreference.ShowBrandedFood).first()
        return foodRepository.getByQuery(query, showCommonFood, showCustomFood, showBrandedFood, page)
    }
}