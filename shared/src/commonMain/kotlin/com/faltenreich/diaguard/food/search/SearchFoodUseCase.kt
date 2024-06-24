package com.faltenreich.diaguard.food.search

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.shared.data.PagingPage

class SearchFoodUseCase(
    private val foodRepository: FoodRepository,
) {

    suspend operator fun invoke(query: String, page: PagingPage): List<Food.Local> {
        return foodRepository.getByQuery(query, page)
    }
}