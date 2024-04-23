package com.faltenreich.diaguard.food.search

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.shared.data.PagingPage
import kotlinx.coroutines.flow.Flow

class SearchFoodUseCase(
    private val foodRepository: FoodRepository,
) {

    operator fun invoke(query: String, page: PagingPage): Flow<List<Food>> {
        return foodRepository.observeByQuery(query, page)
    }
}