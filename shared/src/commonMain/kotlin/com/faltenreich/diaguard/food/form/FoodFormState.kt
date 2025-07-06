package com.faltenreich.diaguard.food.form

import com.faltenreich.diaguard.food.Food

data class FoodFormState(
    val food: Food.Local?,
    val error: String?,
)