package com.faltenreich.diaguard.food.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenFoodFactsNutrients(
    @SerialName("carbohydrates_100g") val carbohydrates: Float?,
    @SerialName("energy_100g") val energy: Float?,
    @SerialName("fat_100g") val fat: Float?,
    @SerialName("saturated-fat_100g") val fatSaturated: Float?,
    @SerialName("fiber_100g") val fiber: Float?,
    @SerialName("proteins_100g") val proteins: Float?,
    @SerialName("salt_100g") val salt: Float?,
    @SerialName("sodium_100g") val sodium: Float?,
    @SerialName("sugars_100g") val sugar: Float?,
)