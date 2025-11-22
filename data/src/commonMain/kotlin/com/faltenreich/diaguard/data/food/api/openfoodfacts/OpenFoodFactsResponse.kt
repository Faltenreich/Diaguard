package com.faltenreich.diaguard.data.food.api.openfoodfacts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class OpenFoodFactsResponse(
    @SerialName("products")
    val products: List<OpenFoodFactsProduct>,
    @SerialName("count")
    val count: Int,
)