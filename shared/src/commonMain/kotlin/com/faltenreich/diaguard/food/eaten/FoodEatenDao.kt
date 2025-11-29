package com.faltenreich.diaguard.food.eaten

import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface FoodEatenDao {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        amountInGrams: Double,
        foodId: Long,
        entryId: Long,
    )

    fun getLastId(): Long?

    fun observeByFoodId(foodId: Long): Flow<List<FoodEaten.Local>>

    fun observeByEntryId(entryId: Long): Flow<List<FoodEaten.Local>>

    fun getByEntryId(entryId: Long): List<FoodEaten.Local>

    fun update(
        id: Long,
        updatedAt: DateTime,
        amountInGrams: Double,
    )

    fun deleteById(id: Long)
}