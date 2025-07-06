package com.faltenreich.diaguard.food.form

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.nutrient.FoodNutrientData

sealed interface FoodFormIntent {

    data class SetName(val name: String) : FoodFormIntent

    data class SetBrand(val brand: String) : FoodFormIntent

    data class SetIngredients(val ingredients: String) : FoodFormIntent

    data class SetLabels(val labels: String) : FoodFormIntent

    data class EditNutrient(val data: FoodNutrientData) : FoodFormIntent

    data class OpenFoodEaten(val food: Food.Local) : FoodFormIntent

    data object Submit : FoodFormIntent

    data object Delete : FoodFormIntent
}