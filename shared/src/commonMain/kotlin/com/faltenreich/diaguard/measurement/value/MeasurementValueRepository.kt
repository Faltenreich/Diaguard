package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class MeasurementValueRepository(
    private val dao: MeasurementValueDao,
) {

    fun create(
        value: Double,
        typeId: Long,
        measurementId: Long,
    ): Long {
        dao.create(
            createdAt = DateTime.now(),
            value = value,
            typeId = typeId,
            measurementId = measurementId,
        )
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun getByMeasurementId(measurementId: Long): Flow<List<MeasurementValue>> {
        return dao.getByMeasurementId(measurementId)
    }

    fun update(
        id: Long,
        value: Double,
    ) {
        dao.update(
            id = id,
            updatedAt = DateTime.now(),
            value = value,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}