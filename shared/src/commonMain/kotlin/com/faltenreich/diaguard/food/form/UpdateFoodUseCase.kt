package com.faltenreich.diaguard.food.form

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.FoodRepository

class UpdateFoodUseCase(
    private val foodRepository: FoodRepository,
) {

    operator fun invoke(food: Food.Local) = with(food) {
        foodRepository.update(
            id = id,
            name = name,
            brand = brand,
            ingredients = ingredients,
            labels = labels,
            carbohydrates = carbohydrates,
            energy = energy,
            fat = fat,
            fatSaturated = fatSaturated,
            fiber = fiber,
            proteins = proteins,
            salt = salt,
            sodium = sodium,
            sugar = sugar,
        )
    }
}