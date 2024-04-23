package com.faltenreich.diaguard.food

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.food.api.FoodFromApi
import kotlinx.coroutines.flow.Flow

interface FoodDao {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        uuid: String?,
        name: String,
        brand: String?,
        ingredients: String?,
        labels: String?,
        carbohydrates: Double,
        energy: Double?,
        fat: Double?,
        fatSaturated: Double?,
        fiber: Double?,
        proteins: Double?,
        salt: Double?,
        sodium: Double?,
        sugar: Double?,
    )

    fun createOrUpdate(
        foodFromApi: List<FoodFromApi>,
        at: DateTime,
    )

    fun getLastId(): Long?

    fun getByUuid(uuid: String): Food?

    fun observeAll(): Flow<List<Food>>

    fun observeByQuery(query: String): Flow<List<Food>>

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        brand: String?,
        ingredients: String?,
        labels: String?,
        carbohydrates: Double,
        energy: Double?,
        fat: Double?,
        fatSaturated: Double?,
        fiber: Double?,
        proteins: Double?,
        salt: Double?,
        sodium: Double?,
        sugar: Double?,
    )

    fun deleteById(id: Long)
}