package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.measurement.Measurement
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class MeasurementValueRepository(
    private val dao: MeasurementValueDao,
) {

    fun create(
        value: Double,
        type: MeasurementType,
        measurement: Measurement,
    ): Long {
        dao.create(
            createdAt = DateTime.now(),
            value = value,
            type = type,
            measurement = measurement,
        )
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun getByMeasurement(measurement: Measurement): Flow<List<MeasurementValue>> {
        return dao.getByMeasurement(measurement)
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