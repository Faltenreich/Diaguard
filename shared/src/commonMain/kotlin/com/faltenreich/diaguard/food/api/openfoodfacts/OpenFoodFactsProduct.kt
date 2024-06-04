package com.faltenreich.diaguard.food.api.openfoodfacts

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
    @SerialName("last_edit_dates_tags") val lastEditDates: List<String>,
    // Nutrients
    @SerialName("carbohydrates_100g") val carbohydrates: Float?,
    @SerialName("energy_100g") val energy: Float?,
    @SerialName("fat_100g") val fat: Float?,
    @SerialName("saturated-fat_100g") val fatSaturated: Float?,
    @SerialName("fiber_100g") val fiber: Float?,
    @SerialName("proteins_100g") val proteins: Float?,
    @SerialName("salt_100g") val salt: Float?,
    @SerialName("sodium_100g") val sodium: Float?,
    @SerialName("sugars_100g") val sugar: Float?,
) {

    companion object {

        // Attention: Add new fields to FIELDS as well

        val FIELDS = listOf(
            "sortkey",
            "lang",
            "product_name",
            "brands",
            "ingredients_text",
            "labels",
            "nutriments",
            "last_edit_dates_tags",
            "carbohydrates_100g",
            "energy_100g",
            "fat_100g",
            "saturated-fat_100g",
            "fiber_100g",
            "proteins_100g",
            "salt_100g",
            "sodium_100g",
            "sugars_100g",
        ).joinToString(",")
    }
}