package com.faltenreich.diaguard.food.form

import com.faltenreich.diaguard.food.Food

data class FoodFormState(
    val food: Food.Local?,
    val name: String,
    val brand: String,
    val ingredients: String,
    val labels: String,
    val error: String?,
)