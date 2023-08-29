package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class MeasurementUnitRepository(
    private val dao: MeasurementUnitDao,
) {

    fun create(
        name: String,
        factor: Double,
        typeId: Long,
    ): Long {
        dao.create(
            createdAt = DateTime.now(),
            name = name,
            factor = factor,
            typeId = typeId,
        )
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun getById(id: Long): Flow<MeasurementUnit?> {
        return dao.getById(id)
    }

    fun getByTypeId(typeId: Long): Flow<List<MeasurementUnit>> {
        return dao.getByTypeId(typeId)
    }

    fun observeAll(): Flow<List<MeasurementUnit>> {
        return dao.observeAll()
    }

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        factor: Double,
    ) {
        dao.update(
            id = id,
            updatedAt = updatedAt,
            name = name,
            factor = factor,
        )
    }

    fun update(unit: MeasurementUnit) = with(unit) {
        update(
            id = id,
            updatedAt = updatedAt,
            name = name,
            factor = factor,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}