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
                val carbohydrates = product.carbohydrates?.toDouble() ?: return@mapNotNull null
                val updatedAt = product.lastEditDates.firstOrNull()?.let(dateTimeFactory::date)
                    ?: return@mapNotNull null
                FoodFromApi(
                    uuid = uuid,
                    updatedAt = updatedAt,
                    name = name,
                    brand = product.brand,
                    ingredients = product.ingredients,
                    labels = product.labels,
                    carbohydrates = carbohydrates,
                    energy = product.energy?.toDouble(),
                    fat = product.fat?.toDouble(),
                    fatSaturated = product.fatSaturated?.toDouble(),
                    fiber = product.fiber?.toDouble(),
                    proteins = product.proteins?.toDouble(),
                    salt = product.salt?.toDouble(),
                    sodium = product.sodium?.toDouble(),
                    sugar = product.sugar?.toDouble(),
                )
            } catch (exception: Exception) {
                Logger.error("Failed to map remote food", exception)
                null
            }
        }
    }
}