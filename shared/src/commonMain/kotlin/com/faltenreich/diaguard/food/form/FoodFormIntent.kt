package com.faltenreich.diaguard.food.form

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.nutrient.FoodNutrientData

sealed interface FoodFormIntent {

    data class EditNutrient(val data: FoodNutrientData) : FoodFormIntent

    data class OpenFoodEaten(val food: Food) : FoodFormIntent

    data object Submit : FoodFormIntent

    data object Delete : FoodFormIntent
}