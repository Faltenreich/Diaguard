package com.faltenreich.diaguard.food.nutrient

data class FoodNutrientData(
    val nutrient: FoodNutrient,
    val per100g: String,
    val isLast: Boolean,
)