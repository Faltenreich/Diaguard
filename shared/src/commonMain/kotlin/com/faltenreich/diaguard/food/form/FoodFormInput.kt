package com.faltenreich.diaguard.food.form

import com.faltenreich.diaguard.food.nutrient.FoodNutrient
import com.faltenreich.diaguard.food.nutrient.FoodNutrientData

data class FoodFormInput(
    val name: String,
    val brand: String,
    val ingredients: String,
    val labels: String,
    val carbohydrates: String,
    val energy: String,
    val fat: String,
    val fatSaturated: String,
    val fiber: String,
    val proteins: String,
    val salt: String,
    val sodium: String,
    val sugar: String,
) {

    val nutrients = NUTRIENTS.mapIndexed { index, nutrient ->
        FoodNutrientData(
            nutrient = nutrient,
            per100g = when (nutrient) {
                FoodNutrient.CARBOHYDRATES -> carbohydrates
                FoodNutrient.SUGAR -> sugar
                FoodNutrient.ENERGY -> energy
                FoodNutrient.FAT -> fat
                FoodNutrient.FAT_SATURATED -> fatSaturated
                FoodNutrient.FIBER -> fiber
                FoodNutrient.PROTEINS -> proteins
                FoodNutrient.SALT -> salt
                FoodNutrient.SODIUM -> sodium
            },
            isLast = index == NUTRIENTS.size - 1,
        )
    }

    companion object {

        private val NUTRIENTS = listOf(
            FoodNutrient.CARBOHYDRATES,
            FoodNutrient.SUGAR,
            FoodNutrient.ENERGY,
            FoodNutrient.FAT,
            FoodNutrient.FAT_SATURATED,
            FoodNutrient.FIBER,
            FoodNutrient.PROTEINS,
            FoodNutrient.SALT,
            FoodNutrient.SODIUM,
        )
    }
}