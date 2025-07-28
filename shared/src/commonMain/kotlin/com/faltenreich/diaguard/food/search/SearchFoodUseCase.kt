package com.faltenreich.diaguard.food.search

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.shared.data.PagingPage

class SearchFoodUseCase(private val repository: FoodRepository) {

    suspend operator fun invoke(
        params: FoodSearchParams,
        page: PagingPage,
    ): List<Food.Localized> {
        return repository.getByQuery(params, page).map { food ->
            with(food) {
                Food.Localized(
                    local = this,
                    // TODO: Format number and decimal places
                    carbohydrates = carbohydrates.toString(),
                    energy = energy?.toString(),
                    fat = fat?.toString(),
                    fatSaturated = fatSaturated?.toString(),
                    fiber = fiber?.toString(),
                    proteins = proteins?.toString(),
                    salt = salt?.toString(),
                    sodium = sodium?.toString(),
                    sugar = sugar?.toString(),
                )
            }
        }
    }
}