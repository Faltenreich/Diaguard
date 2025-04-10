package com.faltenreich.diaguard.food.search

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.shared.data.PagingPage

class SearchFoodUseCase(private val repository: FoodRepository) {

    suspend operator fun invoke(
        params: FoodSearchParams,
        page: PagingPage,
    ): List<Food.Local> {
        return repository.getByQuery(params, page)
    }
}