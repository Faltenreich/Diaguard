package com.faltenreich.diaguard.measurement.category

import androidx.compose.runtime.mutableStateListOf
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class MeasurementCategoryDaoFake : MeasurementCategoryDao {

    private val cache = mutableStateListOf<MeasurementCategory.Local>()

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        key: DatabaseKey.MeasurementCategory?,
        name: String,
        icon: String?,
        sortIndex: Long,
        isActive: Boolean
    ) {
        cache += MeasurementCategory.Local(
            id = cache.size.toLong(),
            createdAt = createdAt,
            updatedAt = updatedAt,
            key = key,
            name = name,
            icon = icon,
            sortIndex = sortIndex,
            isActive = isActive,
        )
    }

    override fun getLastId(): Long? {
        return cache.lastOrNull()?.id
    }

    override fun getById(id: Long): MeasurementCategory.Local? {
        return cache.firstOrNull { it.id == id }
    }

    override fun observeById(id: Long): Flow<MeasurementCategory.Local?> {
        return cache.asFlow().filter { it.id == id }
    }

    override fun observeByKey(key: String): Flow<MeasurementCategory.Local?> {
        return flow { emit(cache.firstOrNull()) }
    }

    override fun observeActive(): Flow<List<MeasurementCategory.Local>> {
        return flow { emit(cache.filter { it.isActive }) }
    }

    override fun observeAll(): Flow<List<MeasurementCategory.Local>> {
        return flow { emit(cache) }
    }

    override fun countAll(): Flow<Long> {
        return flowOf(cache.size.toLong())
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        icon: String?,
        sortIndex: Long,
        isActive: Boolean
    ) {
        val entity = cache.firstOrNull { it.id == id } ?: return
        val index = cache.indexOf(entity)
        cache[index] = entity.copy(
            updatedAt = updatedAt,
            name = name,
            icon = icon,
            sortIndex = sortIndex,
            isActive = isActive,
        )
    }

    override fun deleteById(id: Long) {
        val entry = cache.firstOrNull { it.id == id } ?: return
        cache.remove(entry)
    }
}