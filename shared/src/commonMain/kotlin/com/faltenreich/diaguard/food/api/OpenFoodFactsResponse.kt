package com.faltenreich.diaguard.food.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenFoodFactsResponse(
    @SerialName("products")
    val products: List<OpenFoodFactsProduct>,
    @SerialName("count")
    val count: Int,
)