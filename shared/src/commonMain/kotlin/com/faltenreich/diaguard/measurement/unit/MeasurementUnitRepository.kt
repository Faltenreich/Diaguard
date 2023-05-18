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

    fun getByTypeId(typeId: Long): Flow<List<MeasurementUnit>> {
        return dao.getByTypeId(typeId)
    }

    fun update(
        id: Long,
        name: String,
        factor: Double,
    ) {
        dao.update(
            id = id,
            updatedAt = DateTime.now(),
            name = name,
            factor = factor,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}