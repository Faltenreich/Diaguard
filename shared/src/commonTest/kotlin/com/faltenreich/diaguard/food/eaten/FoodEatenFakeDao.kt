package com.faltenreich.diaguard.food.eaten

import androidx.compose.runtime.mutableStateListOf
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.entry.EntryDao
import com.faltenreich.diaguard.food.FoodDao
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

open class FoodEatenFakeDao(
    private val foodDao: FoodDao = inject(),
    private val entryDao: EntryDao = inject(),
) : FoodEatenDao {

    private val cache = mutableStateListOf<FoodEaten.Local>()

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        amountInGrams: Double,
        foodId: Long,
        entryId: Long
    ) {
        cache += FoodEaten.Local(
            id = cache.size.toLong(),
            createdAt = createdAt,
            updatedAt = updatedAt,
            amountInGrams = amountInGrams,
            food = foodDao.getById(foodId)!!,
            entry = entryDao.getById(entryId)!!,
        )
    }

    override fun getLastId(): Long? {
        return cache.lastOrNull()?.id
    }

    override fun observeByFoodId(foodId: Long): Flow<List<FoodEaten.Local>> {
        return flowOf(cache.filter { it.food.id == foodId })
    }

    override fun observeByEntryId(entryId: Long): Flow<List<FoodEaten.Local>> {
        return flowOf(cache.filter { it.entry.id == entryId })
    }

    override fun getByEntryId(entryId: Long): List<FoodEaten.Local> {
        return cache.filter { it.entry.id == entryId }
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        amountInGrams: Double,
    ) {
        val entity = cache.firstOrNull { it.id == id } ?: return
        val index = cache.indexOf(entity)
        cache[index] = entity.copy(
            updatedAt = updatedAt,
            amountInGrams = amountInGrams,
        )
    }

    override fun deleteById(id: Long) {
        val entry = cache.firstOrNull { it.id == id } ?: return
        cache.remove(entry)
    }
}