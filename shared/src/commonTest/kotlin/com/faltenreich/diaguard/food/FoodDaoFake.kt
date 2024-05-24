package com.faltenreich.diaguard.food

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.shared.data.PagingPage

class FoodDaoFake : FoodDao {

    override fun transaction(transact: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun create(
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
        sugar: Double?
    ) {
        TODO("Not yet implemented")
    }

    override fun getLastId(): Long? {
        TODO("Not yet implemented")
    }

    override fun getByUuid(uuid: String): Food.Local? {
        TODO("Not yet implemented")
    }

    override fun getByUuids(uuids: List<String>): List<String> {
        TODO("Not yet implemented")
    }

    override fun getAll(page: PagingPage): List<Food.Local> {
        TODO("Not yet implemented")
    }

    override fun getByQuery(query: String, page: PagingPage): List<Food.Local> {
        TODO("Not yet implemented")
    }

    override fun update(
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
        sugar: Double?
    ) {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long) {
        TODO("Not yet implemented")
    }
}