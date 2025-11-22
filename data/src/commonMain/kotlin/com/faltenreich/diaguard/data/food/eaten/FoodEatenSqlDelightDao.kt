package com.faltenreich.diaguard.data.food.eaten

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.faltenreich.diaguard.data.SqlDelightDao
import com.faltenreich.diaguard.data.FoodEatenQueries
import com.faltenreich.diaguard.data.SqlDelightApi
import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

internal class FoodEatenSqlDelightDao(
    private val dispatcher: CoroutineDispatcher,
    private val mapper: FoodEatenSqlDelightMapper,
) : FoodEatenDao, SqlDelightDao<FoodEatenQueries> {

    override fun getQueries(api: SqlDelightApi): FoodEatenQueries {
        return api.foodEatenQueries
    }

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        amountInGrams: Double,
        foodId: Long,
        entryId: Long,
    ) {
        queries.create(
            createdAt = createdAt.isoString,
            updatedAt = updatedAt.isoString,
            amountInGrams = amountInGrams,
            foodId = foodId,
            entryId = entryId,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun observeByFoodId(foodId: Long): Flow<List<FoodEaten.Local>> {
        return queries.getByFood(foodId, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun observeByEntryId(entryId: Long): Flow<List<FoodEaten.Local>> {
        return queries.getByEntry(entryId, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun getByEntryId(entryId: Long): List<FoodEaten.Local> {
        return queries.getByEntry(entryId, mapper::map).executeAsList()
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        amountInGrams: Double,
    ) {
        queries.update(
            updatedAt = updatedAt.isoString,
            amountInGrams = amountInGrams,
            id = id,
        )
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}