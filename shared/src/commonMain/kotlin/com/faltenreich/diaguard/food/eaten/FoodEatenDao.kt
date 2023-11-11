package com.faltenreich.diaguard.food.eaten

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface FoodEatenDao {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        amountInGrams: Long,
        foodId: Long,
        entryId: Long,
    )

    fun getLastId(): Long?

    fun observeByFoodId(foodId: Long): Flow<List<FoodEaten>>

    fun observeByEntryId(entryId: Long): Flow<List<FoodEaten>>

    fun update(
        id: Long,
        updatedAt: DateTime,
        amountInGrams: Long,
    )

    fun deleteById(id: Long)
}