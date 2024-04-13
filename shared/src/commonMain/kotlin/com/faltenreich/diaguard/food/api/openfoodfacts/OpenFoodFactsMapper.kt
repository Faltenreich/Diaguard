package com.faltenreich.diaguard.food.api.openfoodfacts

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.logging.Logger
import kotlinx.serialization.json.jsonPrimitive

class OpenFoodFactsMapper(
    private val dateTimeFactory: DateTimeFactory,
) {

    operator fun invoke(response: OpenFoodFactsResponse): List<Food> {
        val now = dateTimeFactory.now()
        return response.products.mapIndexedNotNull { index, product ->
            try {
                val uuid = product.identifier?.jsonPrimitive?.content
                Food(
                    id = index.toLong(), // TODO
                    createdAt = now,
                    updatedAt = now,
                    uuid = uuid,
                    name = product.name!!,
                    brand = product.brand!!,
                    ingredients = product.ingredients,
                    labels = product.labels,
                    carbohydrates = product.nutrients?.carbohydrates?.toDouble() ?: 0.0,
                    energy = product.nutrients?.energy?.toDouble(),
                    fat = product.nutrients?.fat?.toDouble(),
                    fatSaturated = product.nutrients?.fatSaturated?.toDouble(),
                    fiber = product.nutrients?.fiber?.toDouble(),
                    proteins = product.nutrients?.proteins?.toDouble(),
                    salt = product.nutrients?.salt?.toDouble(),
                    sodium = product.nutrients?.sodium?.toDouble(),
                    sugar = product.nutrients?.sugar?.toDouble(),
                )
            } catch (exception: Exception) {
                Logger.error("Failed to map remote food", exception)
                null
            }
        }
    }
}