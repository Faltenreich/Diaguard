package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class MeasurementUnitRepository(
    private val dao: MeasurementUnitDao,
) {

    fun create(
        name: String,
        factor: Double,
        type: MeasurementType,
    ): Long {
        dao.create(
            createdAt = DateTime.now(),
            name = name,
            factor = factor,
            type = type,
        )
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun getAll(): Flow<List<MeasurementUnit>> {
        return dao.getAll()
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