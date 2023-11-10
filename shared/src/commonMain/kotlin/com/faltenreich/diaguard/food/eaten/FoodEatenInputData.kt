package com.faltenreich.diaguard.food.eaten

import com.faltenreich.diaguard.food.Food

data class FoodEatenInputData(
    val food: Food,
    val amountInGrams: Int? = null,
)