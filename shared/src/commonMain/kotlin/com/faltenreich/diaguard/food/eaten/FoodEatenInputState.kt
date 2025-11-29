package com.faltenreich.diaguard.food.eaten

import com.faltenreich.diaguard.food.Food

data class FoodEatenInputState(
    val food: Food.Local,
    val amountPer100g: String,
    val amountInGrams: String,
)