package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class MeasurementUnitRepository(
    private val dao: MeasurementUnitDao,
) {

    fun create(
        name: String,
    ): Long {
        dao.create(
            createdAt = DateTime.now(),
            name = name,
        )
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun getById(id: Long): Flow<MeasurementUnit?> {
        return dao.getById(id)
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