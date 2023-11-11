package com.faltenreich.diaguard.food.eaten

import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class FoodEatenRepository(
    private val dao: FoodEatenDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        amountInGrams: Double,
        foodId: Long,
        entryId: Long,
    ): Long {
        dao.create(
            createdAt = createdAt,
            updatedAt = updatedAt,
            amountInGrams = amountInGrams,
            foodId = foodId,
            entryId = entryId,
        )
        return checkNotNull(dao.getLastId())
    }

    fun observeByFoodId(foodId: Long): Flow<List<FoodEaten>> {
        return dao.observeByFoodId(foodId)
    }

    fun observeByEntryId(entryId: Long): Flow<List<FoodEaten>> {
        return dao.observeByEntryId(entryId)
    }

    fun update(
        id: Long,
        amountInGrams: Double,
    ) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            amountInGrams = amountInGrams,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}