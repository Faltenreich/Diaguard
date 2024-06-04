package com.faltenreich.diaguard.food.api.openfoodfacts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class OpenFoodFactsProduct(
    // Workaround: Sometimes a Number, sometimes a String
    @SerialName(IDENTIFIER) val identifier: JsonElement?,
    @SerialName(LANGUAGE_CODE) val languageCode: String?,
    @SerialName(NAME) val name: String?,
    @SerialName(BRAND) val brand: String?,
    @SerialName(INGREDIENTS) val ingredients: String?,
    @SerialName(LABELS) val labels: String?,
    @SerialName(LAST_EDIT_DATES) val lastEditDates: List<String>,
    // Nutrients
    @SerialName(CARBOHYDRATES) val carbohydrates: Float?,
    @SerialName(ENERGY) val energy: Float?,
    @SerialName(FAT) val fat: Float?,
    @SerialName(FAT_SATURATED) val fatSaturated: Float?,
    @SerialName(FIBER) val fiber: Float?,
    @SerialName(PROTEINS) val proteins: Float?,
    @SerialName(SALT) val salt: Float?,
    @SerialName(SODIUM) val sodium: Float?,
    @SerialName(SUGAR) val sugar: Float?,
) {

    companion object {

        // Attention: Add new fields to FIELDS as well
        private const val IDENTIFIER = "sortkey"
        private const val LANGUAGE_CODE = "lang"
        private const val NAME = "product_name"
        private const val BRAND = "brands"
        private const val INGREDIENTS = "ingredients_text"
        private const val LABELS = "labels"
        private const val LAST_EDIT_DATES = "last_edit_dates_tags"
        private const val CARBOHYDRATES = "carbohydrates_100g"
        private const val ENERGY = "energy_100g"
        private const val FAT = "fat_100g"
        private const val FAT_SATURATED = "saturated-fat_100g"
        private const val FIBER = "fiber_100g"
        private const val PROTEINS = "proteins_100g"
        private const val SALT = "salt_100g"
        private const val SODIUM = "sodium_100g"
        private const val SUGAR = "sugars_100g"

        val Fields = listOf(
            IDENTIFIER,
            LANGUAGE_CODE,
            NAME,
            BRAND,
            INGREDIENTS,
            LABELS,
            LAST_EDIT_DATES,
            CARBOHYDRATES,
            ENERGY,
            FAT,
            FAT_SATURATED,
            FIBER,
            PROTEINS,
            SALT,
            SODIUM,
            SUGAR,
        )
    }
}