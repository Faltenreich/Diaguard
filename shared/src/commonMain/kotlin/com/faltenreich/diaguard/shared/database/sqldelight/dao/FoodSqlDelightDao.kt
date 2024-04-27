package com.faltenreich.diaguard.shared.database.sqldelight.dao

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.FoodDao
import com.faltenreich.diaguard.food.api.FoodFromApi
import com.faltenreich.diaguard.shared.data.PagingPage
import com.faltenreich.diaguard.shared.database.sqldelight.FoodQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.FoodSqlDelightMapper
import com.faltenreich.diaguard.shared.di.inject

class FoodSqlDelightDao(
    private val mapper: FoodSqlDelightMapper = inject(),
) : FoodDao, SqlDelightDao<FoodQueries> {

    override fun getQueries(api: SqlDelightApi): FoodQueries {
        return api.foodQueries
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
        queries.create(
            createdAt = createdAt.isoString,
            updatedAt = updatedAt.isoString,
            uuid = uuid,
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

    override fun createOrUpdate(
        foodList: List<FoodFromApi>,
        updatedAt: DateTime,
    ) {
        transaction {
            foodList.forEach { food ->
                val existing = getByUuid(food.uuid)
                if (existing != null) {
                    update(
                        id = existing.id,
                        updatedAt = updatedAt,
                        name = food.name,
                        brand = food.brand,
                        ingredients = food.ingredients,
                        labels = food.labels,
                        carbohydrates = food.carbohydrates,
                        energy = food.energy,
                        fat = food.fat,
                        fatSaturated = food.fatSaturated,
                        fiber = food.fiber,
                        proteins = food.proteins,
                        salt = food.salt,
                        sodium = food.sodium,
                        sugar = food.sugar,
                    )
                } else {
                    create(
                        createdAt = updatedAt,
                        updatedAt = updatedAt,
                        uuid = food.uuid,
                        name = food.name,
                        brand = food.brand,
                        ingredients = food.ingredients,
                        labels = food.labels,
                        carbohydrates = food.carbohydrates,
                        energy = food.energy,
                        fat = food.fat,
                        fatSaturated = food.fatSaturated,
                        fiber = food.fiber,
                        proteins = food.proteins,
                        salt = food.salt,
                        sodium = food.sodium,
                        sugar = food.sugar,
                    )
                }
            }
        }
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun getByUuid(uuid: String): Food? {
        return queries.getByUuid(uuid, mapper::map).executeAsOneOrNull()
    }

    override fun getAll(page: PagingPage): List<Food> {
        return queries.getAll(
            offset = page.page.toLong() * page.pageSize.toLong(),
            limit = page.pageSize.toLong(),
            mapper = mapper::map,
        ).executeAsList()
    }

    override fun getByQuery(query: String, page: PagingPage): List<Food> {
        return queries.getByQuery(
            query = query,
            offset = page.page.toLong() * page.pageSize.toLong(),
            limit = page.pageSize.toLong(),
            mapper = mapper::map,
        ).executeAsList()
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
        queries.update(
            updatedAt = updatedAt.isoString,
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
            id = id,
        )
    }


    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}