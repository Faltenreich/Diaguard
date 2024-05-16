package com.faltenreich.diaguard.food.form

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.FoodRepository

class CreateFoodUseCase(
    private val foodRepository: FoodRepository,
) {

    operator fun invoke(food: Food.User) {
        foodRepository.create(food)
    }
}