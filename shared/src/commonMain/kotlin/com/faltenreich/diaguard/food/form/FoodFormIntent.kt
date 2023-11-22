package com.faltenreich.diaguard.food.form

import com.faltenreich.diaguard.food.nutrient.FoodNutrientData

sealed interface FoodFormIntent {

    data class EditNutrient(val data: FoodNutrientData) : FoodFormIntent

    data object Submit : FoodFormIntent

    data object Delete : FoodFormIntent
}