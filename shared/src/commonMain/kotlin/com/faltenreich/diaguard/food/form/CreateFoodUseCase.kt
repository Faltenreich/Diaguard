package com.faltenreich.diaguard.food.form

import com.faltenreich.diaguard.food.FoodRepository

class CreateFoodUseCase(
    private val foodRepository: FoodRepository,
) {

    operator fun invoke(id: Long?, input: FoodInput) = with(input) {
        // TODO: Avoid force unwraps
        val name = name!!
        val carbohydrates = carbohydrates!!
        when (id) {
            null -> foodRepository.create(
                uuid = null,
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
            else -> foodRepository.update(
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
}