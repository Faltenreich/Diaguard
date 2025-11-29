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
            carbohydrates = food?.carbohydrates.formatted(),
            sugar = food?.sugar.formatted(),
            energy = food?.energy.formatted(),
            fat = food?.fat.formatted(),
            fatSaturated = food?.fatSaturated.formatted(),
            fiber = food?.fiber.formatted(),
            proteins = food?.proteins.formatted(),
            salt = food?.salt.formatted(),
            sodium = food?.sodium.formatted(),
        )
    }

    private fun Double?.formatted(): String {
        this ?: return ""
        return formatNumber(
            number = this,
            scale = SCALE,
        )
    }

    companion object {

        private const val SCALE = 3
    }
}