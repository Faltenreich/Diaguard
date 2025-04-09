package com.faltenreich.diaguard.measurement.unit

import androidx.compose.runtime.mutableStateListOf
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MeasurementUnitFakeDao : MeasurementUnitDao {

    private val cache = mutableStateListOf<MeasurementUnit.Local>()

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        key: DatabaseKey.MeasurementUnit?,
        name: String,
        abbreviation: String,
    ) {
        cache += MeasurementUnit.Local(
            id = cache.size.toLong(),
            createdAt = createdAt,
            updatedAt = updatedAt,
            key = key,
            name = name,
            abbreviation = abbreviation,
        )
    }

    override fun getLastId(): Long? {
        return cache.lastOrNull()?.id
    }

    override fun getById(id: Long): MeasurementUnit.Local? {
        return cache.firstOrNull { it.id == id }
    }

    override fun observeById(id: Long): Flow<MeasurementUnit.Local?> {
        return flowOf(cache.firstOrNull { it.id == id })
    }

    override fun observeAll(): Flow<List<MeasurementUnit.Local>> {
        return flowOf(cache)
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        abbreviation: String,
    ) {
        val entity = cache.firstOrNull { it.id == id } ?: return
        val index = cache.indexOf(entity)
        cache[index] = entity.copy(
            updatedAt = updatedAt,
            name = name,
            abbreviation = abbreviation,
        )
    }

    override fun deleteById(id: Long) {
        val entry = cache.firstOrNull { it.id == id } ?: return
        cache.remove(entry)
    }
}