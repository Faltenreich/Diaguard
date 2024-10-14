package com.faltenreich.diaguard.measurement.unit

import androidx.compose.runtime.mutableStateListOf
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyDao
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyFakeDao
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

open class MeasurementUnitFakeDao(
    private val propertyDao: MeasurementPropertyDao = inject(),
) : MeasurementUnitDao {

    private val cache = mutableStateListOf<MeasurementUnit.Local>()

    override fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        key: DatabaseKey.MeasurementUnit?,
        name: String,
        abbreviation: String,
        factor: Double,
        isSelected: Boolean,
        propertyId: Long
    ) {
        cache += MeasurementUnit.Local(
            id = cache.size.toLong(),
            createdAt = createdAt,
            updatedAt = updatedAt,
            key = key,
            name = name,
            abbreviation = abbreviation,
            factor = factor,
            isSelected = isSelected,
            property = propertyDao.getById(propertyId)!!,
        ).also { unit ->
            (propertyDao as MeasurementPropertyFakeDao).updateSelectedUnit(unit)
        }
    }

    override fun getLastId(): Long? {
        return cache.lastOrNull()?.id
    }

    override fun observeById(id: Long): Flow<MeasurementUnit.Local?> {
        return flowOf(cache.firstOrNull { it.id == id })
    }

    override fun getByKey(key: String): MeasurementUnit.Local? {
        return cache.firstOrNull { it.key?.key == key }
    }

    override fun observeByPropertyId(propertyId: Long): Flow<List<MeasurementUnit.Local>> {
        return flowOf(cache.filter { it.property.id == propertyId })
    }

    override fun observeByCategoryId(categoryId: Long): Flow<List<MeasurementUnit.Local>> {
        return flowOf(cache.filter { it.property.category.id == categoryId })
    }

    override fun observeAll(): Flow<List<MeasurementUnit.Local>> {
        return flowOf(cache)
    }

    override fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        abbreviation: String,
        isSelected: Boolean
    ) {
        val entity = cache.firstOrNull { it.id == id } ?: return
        val index = cache.indexOf(entity)
        cache[index] = entity.copy(
            updatedAt = updatedAt,
            name = name,
            abbreviation = abbreviation,
            isSelected = isSelected,
        )
    }

    override fun deleteById(id: Long) {
        val entry = cache.firstOrNull { it.id == id } ?: return
        cache.remove(entry)
    }
}