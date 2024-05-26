package com.faltenreich.diaguard.food.eaten

import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class FoodEatenDaoFake : FoodEatenDao {

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        amountInGrams: Long,
        foodId: Long,
        entryId: Long
    ) {
        TODO("Not yet implemented")
    }

    override fun getLastId(): Long? {
        TODO("Not yet implemented")
    }

    override fun observeByFoodId(foodId: Long): Flow<List<FoodEaten.Local>> {
        TODO("Not yet implemented")
    }

    override fun observeByEntryId(entryId: Long): Flow<List<FoodEaten.Local>> {
        TODO("Not yet implemented")
    }

    override fun getByEntryId(entryId: Long): List<FoodEaten.Local> {
        TODO("Not yet implemented")
    }

    override fun update(id: Long, updatedAt: DateTime, amountInGrams: Long) {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long) {
        TODO("Not yet implemented")
    }
}