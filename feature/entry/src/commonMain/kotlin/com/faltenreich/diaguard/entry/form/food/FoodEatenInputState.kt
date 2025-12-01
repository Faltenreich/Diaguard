package com.faltenreich.diaguard.entry.form.food

import com.faltenreich.diaguard.data.food.Food

data class FoodEatenInputState(
    val food: Food.Local,
    val amountPer100g: String,
    val amountInGrams: String,
)