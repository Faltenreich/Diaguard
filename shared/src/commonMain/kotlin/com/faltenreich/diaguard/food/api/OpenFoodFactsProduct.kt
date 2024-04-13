package com.faltenreich.diaguard.food.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class OpenFoodFactsProduct(
    // Workaround: Sometimes a Number, sometimes a String
    @SerialName("sortkey") val identifier: JsonElement?,
    @SerialName("lang") val languageCode: String?,
    @SerialName("product_name") val name: String?,
    @SerialName("brands") val brand: String?,
    @SerialName("ingredients_text") val ingredients: String?,
    @SerialName("labels") val labels: String?,
    @SerialName("nutriments") val nutrients: OpenFoodFactsNutrients?,
    @SerialName("last_edit_dates_tags") val lastEditDates: List<String>,
) {

    val isValid: Boolean
        get() = name?.isNotBlank() == true && nutrients?.carbohydrates != null

    companion object {

        const val DATE_FORMAT = "yyyy-MM-dd"
    }
}
