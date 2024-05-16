package com.faltenreich.diaguard.food

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.shared.data.PagingPage

interface FoodDao {

    fun transaction(transact: () -> Unit)

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

    fun getLastId(): Long?

    fun getByUuid(uuid: String): Food.Local?

    fun getByUuids(uuids: List<String>): List<String>

    fun getAll(page: PagingPage): List<Food.Local>

    fun getByQuery(query: String, page: PagingPage): List<Food.Local>

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