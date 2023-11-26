package com.faltenreich.diaguard.food.form

import com.faltenreich.diaguard.food.FoodRepository

class DeleteFoodUseCase(
    private val foodRepository: FoodRepository,
) {

    operator fun invoke(id: Long) {
        foodRepository.deleteById(id)
    }
}