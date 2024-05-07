package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class MeasurementUnitRepository(
    private val dao: MeasurementUnitDao,
) {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        name: String,
        abbreviation: String,
        key: String?,
        factor: Double,
        propertyId: Long,
    ): Long {
        dao.create(
            createdAt = createdAt,
            updatedAt = updatedAt,
            key = key,
            name = name,
            abbreviation = abbreviation,
            factor = factor,
            propertyId = propertyId,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getByKey(key: String): MeasurementUnit {
        return checkNotNull(dao.getByKey(key))
    }

    fun observeByPropertyId(propertyId: Long): Flow<List<MeasurementUnit>> {
        return dao.observeByPropertyId(propertyId)
    }

    fun observeByCategoryId(categoryId: Long): Flow<List<MeasurementUnit>> {
        return dao.observeByCategoryId(categoryId)
    }

    fun observeAll(): Flow<List<MeasurementUnit>> {
        return dao.observeAll()
    }

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        abbreviation: String,
    ) {
        dao.update(
            id = id,
            updatedAt = updatedAt,
            name = name,
            abbreviation = abbreviation,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}