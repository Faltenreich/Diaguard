package com.faltenreich.diaguard.food.input

import com.faltenreich.diaguard.food.Food

data class FoodInputData(
    val food: Food,
    val amountInGrams: Int? = null,
)