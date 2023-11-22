package com.faltenreich.diaguard.food.form

import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory

class CreateFoodUseCase(
    private val foodRepository: FoodRepository,
    private val dateTimeFactory: DateTimeFactory,
) {

    operator fun invoke(
        id: Long?,
        name: String,
        carbohydrates: Double,
        brand: String?,
        ingredients: String?,
        labels: String?,
        energy: Double?,
        fat: Double?,
        fatSaturated: Double?,
        fiber: Double?,
        proteins: Double?,
        salt: Double?,
        sodium: Double?,
        sugar: Double?,
    ) {
        val now = dateTimeFactory.now()
        when (id) {
            null -> foodRepository.create(
                createdAt = now,
                updatedAt = now,
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
                updatedAt = now,
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