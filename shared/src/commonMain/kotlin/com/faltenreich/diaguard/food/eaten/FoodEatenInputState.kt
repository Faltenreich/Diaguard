package com.faltenreich.diaguard.food.eaten

import com.faltenreich.diaguard.food.Food

data class FoodEatenInputState(
    val food: Food.Local,
    val amountInGrams: Long? = null,
)