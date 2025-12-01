package com.faltenreich.diaguard.food.form

import com.faltenreich.diaguard.data.food.Food
import com.faltenreich.diaguard.data.food.FoodRepository

class DeleteFoodUseCase(private val repository: FoodRepository) {

    operator fun invoke(food: Food.Local) {
        repository.delete(food)
    }
}