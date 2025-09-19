package com.faltenreich.diaguard.food.form

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.nutrient.FoodNutrientData

sealed interface FoodFormIntent {

    data class SetInput(val input: FoodFormInput) : FoodFormIntent

    data class SetNutrient(val data: FoodNutrientData) : FoodFormIntent

    data class OpenFoodEaten(val food: Food.Local) : FoodFormIntent

    data object Submit : FoodFormIntent

    data object Delete : FoodFormIntent
}