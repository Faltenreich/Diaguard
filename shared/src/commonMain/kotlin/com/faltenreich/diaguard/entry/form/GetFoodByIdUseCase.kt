package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.FoodRepository

class GetFoodByIdUseCase(
    private val foodRepository: FoodRepository,
) {

    operator fun invoke(id: Long): Food.Local? {
        return foodRepository.getById(id)
    }
}