package com.faltenreich.diaguard.entry.form

import com.faltenreich.diaguard.data.food.Food
import com.faltenreich.diaguard.data.food.FoodRepository

class GetFoodByIdUseCase(
    private val foodRepository: FoodRepository,
) {

    operator fun invoke(id: Long): Food.Local? {
        return foodRepository.getById(id)
    }
}