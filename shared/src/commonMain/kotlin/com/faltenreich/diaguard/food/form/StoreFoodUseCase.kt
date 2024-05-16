package com.faltenreich.diaguard.food.form

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.FoodRepository

class StoreFoodUseCase(private val repository: FoodRepository) {

    operator fun invoke(food: Food) {
        when (food) {
            is Food.User -> repository.create(food)
            is Food.Remote -> repository.create(food)
            is Food.Local -> repository.update(food)
        }
    }
}