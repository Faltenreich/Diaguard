package com.faltenreich.diaguard.food.search

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.FoodRepository
import kotlinx.coroutines.flow.Flow

class SearchFoodUseCase(
    private val foodRepository: FoodRepository,
) {

    operator fun invoke(query: String): Flow<List<Food>> {
        return foodRepository.observeByQuery(query)
    }
}