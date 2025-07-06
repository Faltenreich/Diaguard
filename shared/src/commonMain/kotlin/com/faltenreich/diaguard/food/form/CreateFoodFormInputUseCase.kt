package com.faltenreich.diaguard.food.form

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.localization.NumberFormatter

class CreateFoodFormInputUseCase(
    private val formatNumber: NumberFormatter,
) {

    operator fun invoke(food: Food?): FoodFormInput {
        return FoodFormInput(
            name = food?.name ?: "",
            brand = food?.brand ?: "",
            ingredients = food?.ingredients ?: "",
            labels = food?.labels ?: "",
            carbohydrates = food?.carbohydrates?.let(::formatNutrient) ?: "",
            sugar = food?.sugar?.let(::formatNutrient) ?: "",
            energy = food?.energy?.let(::formatNutrient) ?: "",
            fat = food?.fat?.let(::formatNutrient) ?: "",
            fatSaturated = food?.fatSaturated?.let(::formatNutrient) ?: "",
            fiber = food?.fiber?.let(::formatNutrient) ?: "",
            proteins = food?.proteins?.let(::formatNutrient) ?: "",
            salt = food?.salt?.let(::formatNutrient) ?: "",
            sodium = food?.sodium?.let(::formatNutrient) ?: "",
        )
    }

    private fun formatNutrient(nutrient: Double): String {
        return formatNumber(
            number = nutrient,
            scale = SCALE,
        )
    }

    companion object {

        private const val SCALE = 3
    }
}