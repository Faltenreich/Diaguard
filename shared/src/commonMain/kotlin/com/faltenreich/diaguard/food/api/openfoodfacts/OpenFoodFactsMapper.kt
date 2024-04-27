package com.faltenreich.diaguard.food.api.openfoodfacts

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.food.api.FoodFromApi
import com.faltenreich.diaguard.shared.logging.Logger
import kotlinx.serialization.json.jsonPrimitive

class OpenFoodFactsMapper(
    private val dateTimeFactory: DateTimeFactory
) {

    operator fun invoke(remote: List<OpenFoodFactsProduct>): List<FoodFromApi> {
        return remote.mapNotNull { product ->
            try {
                val uuid = product.identifier?.jsonPrimitive?.content ?: return@mapNotNull null
                val name = product.name ?: return@mapNotNull null
                val nutrients = product.nutrients ?: return@mapNotNull null
                val carbohydrates = nutrients.carbohydrates?.toDouble() ?: return@mapNotNull null
                val updatedAt = product.lastEditDates.firstOrNull()?.let(dateTimeFactory::date) ?: return@mapNotNull null
                FoodFromApi(
                    uuid = uuid,
                    updatedAt = updatedAt,
                    name = name,
                    brand = product.brand,
                    ingredients = product.ingredients,
                    labels = product.labels,
                    carbohydrates = carbohydrates,
                    energy = nutrients.energy?.toDouble(),
                    fat = nutrients.fat?.toDouble(),
                    fatSaturated = nutrients.fatSaturated?.toDouble(),
                    fiber = nutrients.fiber?.toDouble(),
                    proteins = nutrients.proteins?.toDouble(),
                    salt = nutrients.salt?.toDouble(),
                    sodium = nutrients.sodium?.toDouble(),
                    sugar = nutrients.sugar?.toDouble(),
                )
            } catch (exception: Exception) {
                Logger.error("Failed to map remote food", exception)
                null
            }
        }
    }
}