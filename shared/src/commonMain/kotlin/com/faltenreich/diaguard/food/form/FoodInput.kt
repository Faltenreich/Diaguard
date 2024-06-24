package com.faltenreich.diaguard.food.form

data class FoodInput(
    val name: String?,
    val carbohydrates: Double?,
    val brand: String?,
    val ingredients: String?,
    val labels: String?,
    val energy: Double?,
    val fat: Double?,
    val fatSaturated: Double?,
    val fiber: Double?,
    val proteins: Double?,
    val salt: Double?,
    val sodium: Double?,
    val sugar: Double?,
)