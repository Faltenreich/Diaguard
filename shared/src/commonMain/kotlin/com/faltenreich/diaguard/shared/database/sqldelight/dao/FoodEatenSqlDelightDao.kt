package com.faltenreich.diaguard.shared.database.sqldelight.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.food.eaten.FoodEatenDao
import com.faltenreich.diaguard.shared.database.sqldelight.FoodEatenQueries
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightApi
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.FoodEatenSqlDelightMapper
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class FoodEatenSqlDelightDao(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val mapper: FoodEatenSqlDelightMapper = inject(),
) : FoodEatenDao, SqlDelightDao<FoodEatenQueries> {

    override fun getQueries(api: SqlDelightApi): FoodEatenQueries {
        return api.foodEatenQueries
    }

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        amountInGrams: Long,
        foodId: Long,
        entryId: Long,
    ) {
        queries.create(
            created_at = createdAt.isoString,
            updated_at = updatedAt.isoString,
            amount_in_grams = amountInGrams,
            food_id = foodId,
            entry_id = entryId,
        )
    }

    override fun getLastId(): Long? {
        return queries.getLastId().executeAsOneOrNull()
    }

    override fun observeByFoodId(foodId: Long): Flow<List<FoodEaten>> {
        return queries.getByFood(foodId, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun observeByEntryId(entryId: Long): Flow<List<FoodEaten>> {
        return queries.getByEntry(entryId, mapper::map).asFlow().mapToList(dispatcher)
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        amountInGrams: Long,
    ) {
        queries.update(
            updated_at = updatedAt.isoString,
            amount_in_grams = amountInGrams,
            id = id,
        )
    }

    override fun deleteById(id: Long) {
        queries.deleteById(id)
    }
}