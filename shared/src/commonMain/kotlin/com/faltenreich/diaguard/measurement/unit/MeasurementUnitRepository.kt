package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class MeasurementUnitRepository(
    private val dao: MeasurementUnitDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        name: String,
        abbreviation: String,
        key: String?,
        factor: Double,
        typeId: Long,
    ): Long {
        dao.create(
            createdAt = dateTimeFactory.now(),
            key = key,
            name = name,
            abbreviation = abbreviation,
            factor = factor,
            typeId = typeId,
        )
        return dao.getLastId() ?: throw IllegalStateException("Missing previously created entity")
    }

    fun observeById(id: Long): Flow<MeasurementUnit?> {
        return dao.observeById(id)
    }

    fun getByKey(key: String): MeasurementUnit {
        return dao.getByKey(key) ?: throw IllegalStateException("Missing entity for key: $key")
    }

    fun observeByTypeId(typeId: Long): Flow<List<MeasurementUnit>> {
        return dao.observeByTypeId(typeId)
    }

    fun observeByPropertyId(propertyId: Long): Flow<List<MeasurementUnit>> {
        return dao.observeByPropertyId(propertyId)
    }

    fun observeAll(): Flow<List<MeasurementUnit>> {
        return dao.observeAll()
    }

    fun update(
        id: Long,
        name: String,
        abbreviation: String,
    ) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            name = name,
            abbreviation = abbreviation,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}