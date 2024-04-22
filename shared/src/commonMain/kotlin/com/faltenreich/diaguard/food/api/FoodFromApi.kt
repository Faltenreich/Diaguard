package com.faltenreich.diaguard.food.api

data class FoodFromApi(
    val uuid: String,
    val name: String,
    val brand: String?,
    val ingredients: String?,
    val labels: String?,
    val carbohydrates: Double,
    val energy: Double?,
    val fat: Double?,
    val fatSaturated: Double?,
    val fiber: Double?,
    val proteins: Double?,
    val salt: Double?,
    val sodium: Double?,
    val sugar: Double?,
)