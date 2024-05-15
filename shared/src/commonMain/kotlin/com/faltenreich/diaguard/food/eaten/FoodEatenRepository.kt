package com.faltenreich.diaguard.food.eaten

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class FoodEatenRepository(
    private val dao: FoodEatenDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        amountInGrams: Long,
        foodId: Long,
        entryId: Long,
    ): Long {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            amountInGrams = amountInGrams,
            foodId = foodId,
            entryId = entryId,
        )
        return checkNotNull(dao.getLastId())
    }

    fun observeByFoodId(foodId: Long): Flow<List<FoodEaten>> {
        return dao.observeByFoodId(foodId)
    }

    fun getByEntryId(entryId: Long): List<FoodEaten> {
        return dao.getByEntryId(entryId)
    }

    fun update(
        id: Long,
        amountInGrams: Long,
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