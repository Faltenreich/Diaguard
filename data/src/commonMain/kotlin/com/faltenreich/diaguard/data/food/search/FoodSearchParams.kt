package com.faltenreich.diaguard.data.food.search

data class FoodSearchParams(
    val query: String,
    val showCommonFood: Boolean,
    val showCustomFood: Boolean,
    val showBrandedFood: Boolean,
)