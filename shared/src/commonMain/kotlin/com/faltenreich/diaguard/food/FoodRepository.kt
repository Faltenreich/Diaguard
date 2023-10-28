package com.faltenreich.diaguard.food

import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class FoodRepository(
    private val dao: FoodDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        name: String,
        carbohydrates: Double,
        brand: String? = null,
        ingredients: String? = null,
        labels: String? = null,
        energy: Double? = null,
        fat: Double? = null,
        fatSaturated: Double? = null,
        fiber: Double? = null,
        proteins: Double? = null,
        salt: Double? = null,
        sodium: Double? = null,
        sugar: Double? = null,
    ): Long {
        dao.create(
            createdAt = createdAt,
            updatedAt = updatedAt,
            name = name,
            brand = brand,
            ingredients = ingredients,
            labels = labels,
            carbohydrates = carbohydrates,
            energy = energy,
            fat = fat,
            fatSaturated = fatSaturated,
            fiber = fiber,
            proteins = proteins,
            salt = salt,
            sodium = sodium,
            sugar = sugar,
        )
        return dao.getLastId() ?: throw IllegalStateException("Missing previously created entity")
    }

    fun create(
        name: String,
        carbohydrates: Double,
    ): Long {
        val now = dateTimeFactory.now()
        return create(
            createdAt = now,
            updatedAt = now,
            name = name,
            carbohydrates = carbohydrates,
        )
    }

    fun observeAll(): Flow<List<Food>> {
        return dao.observeAll()
    }

    fun observeByQuery(query: String): Flow<List<Food>> {
        return dao.observeByQuery(query)
    }

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        carbohydrates: Double,
        brand: String? = null,
        ingredients: String? = null,
        labels: String? = null,
        energy: Double? = null,
        fat: Double? = null,
        fatSaturated: Double? = null,
        fiber: Double? = null,
        proteins: Double? = null,
        salt: Double? = null,
        sodium: Double? = null,
        sugar: Double? = null,
    ) {
        dao.update(
            id = id,
            updatedAt = updatedAt,
            name = name,
            brand = brand,
            ingredients = ingredients,
            labels = labels,
            carbohydrates = carbohydrates,
            energy = energy,
            fat = fat,
            fatSaturated = fatSaturated,
            fiber = fiber,
            proteins = proteins,
            salt = salt,
            sodium = sodium,
            sugar = sugar,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}