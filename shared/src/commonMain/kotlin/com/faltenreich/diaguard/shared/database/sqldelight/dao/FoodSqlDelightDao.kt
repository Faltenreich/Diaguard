package com.faltenreich.diaguard.shared.database.sqldelight.dao

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.FoodDao
import com.faltenreich.diaguard.shared.data.PagingPage
import com.faltenreich.diaguard.shared.database.sqldelight.FoodQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightExtensions.toSqlLiteLong
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.FoodSqlDelightMapper

class FoodSqlDelightDao(
    private val mapper: FoodSqlDelightMapper,
) : FoodDao, SqlDelightDao<FoodQueries> {

    override fun getQueries(api: SqlDelightApi): FoodQueries {
        return api.foodQueries
    }

    override fun transaction(transact: () -> Unit) {
        queries.transaction { transact() }
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

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun getByUuid(uuid: String): Food.Local? {
        return queries.getByUuid(uuid, mapper::map).executeAsOneOrNull()
    }

    override fun getById(id: Long): Food.Local? {
        return queries.getById(id, mapper::map).executeAsOneOrNull()
    }

    override fun getByUuids(uuids: List<String>): List<String> {
        return queries.getByUuids(uuids).executeAsList().mapNotNull { it.uuid }
    }

    override fun getAll(
        showCommonFood: Boolean,
        showCustomFood: Boolean,
        showBrandedFood: Boolean,
        page: PagingPage,
    ): List<Food.Local> {
        return queries.getAll(
            showCommonFood = showCommonFood.toSqlLiteLong(),
            showCustomFood = showCustomFood.toSqlLiteLong(),
            showBrandedFood = showBrandedFood.toSqlLiteLong(),
            offset = page.page.toLong() * page.pageSize.toLong(),
            limit = page.pageSize.toLong(),
            mapper = mapper::map,
        ).executeAsList()
    }

    override fun getByQuery(
        query: String,
        showCommonFood: Boolean,
        showCustomFood: Boolean,
        showBrandedFood: Boolean,
        page: PagingPage,
    ): List<Food.Local> {
        return queries.getByQuery(
            query = query,
            showCommonFood = showCommonFood.toSqlLiteLong(),
            showCustomFood = showCustomFood.toSqlLiteLong(),
            showBrandedFood = showBrandedFood.toSqlLiteLong(),
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