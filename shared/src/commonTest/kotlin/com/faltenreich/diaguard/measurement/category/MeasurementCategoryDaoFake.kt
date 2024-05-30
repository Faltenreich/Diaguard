package com.faltenreich.diaguard.measurement.category

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MeasurementCategoryDaoFake : MeasurementCategoryDao {

    private val cache = MutableStateFlow<List<MeasurementCategory.Local>>(emptyList())

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        key: DatabaseKey.MeasurementCategory?,
        name: String,
        icon: String?,
        sortIndex: Long,
        isActive: Boolean
    ) {
        cache.value += MeasurementCategory.Local(
            id = cache.value.size.toLong(),
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
        return cache.value.lastOrNull()?.id
    }

    override fun getById(id: Long): MeasurementCategory.Local? {
        return cache.value.firstOrNull { it.id == id }
    }

    override fun observeById(id: Long): Flow<MeasurementCategory.Local?> {
        return cache.map { it.firstOrNull { it.id == id } }
    }

    override fun observeByKey(key: String): Flow<MeasurementCategory.Local?> {
        return cache.map { it.firstOrNull { it.key?.key == key } }
    }

    override fun observeActive(): Flow<List<MeasurementCategory.Local>> {
        return cache.map { it.filter { it.isActive } }
    }

    override fun observeAll(): Flow<List<MeasurementCategory.Local>> {
        return cache
    }

    override fun countAll(): Flow<Long> {
        return cache.map { it.size.toLong() }
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        icon: String?,
        sortIndex: Long,
        isActive: Boolean
    ) {
        cache.value = cache.value.map { entity ->
            when (entity.id) {
                id -> entity.copy(
                    updatedAt = updatedAt,
                    name = name,
                    icon = icon,
                    sortIndex = sortIndex,
                    isActive = isActive,
                )
                else -> entity
            }
        }
    }

    override fun deleteById(id: Long) {
        cache.value = cache.value.filterNot { it.id == id }
    }
}