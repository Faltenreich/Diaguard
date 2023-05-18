package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.measurement.Measurement
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface MeasurementValueDao {

    fun create(
        createdAt: DateTime,
        value: Double,
        type: MeasurementType,
        measurement: Measurement,
    )

    fun getLastId(): Long?

    fun getByMeasurement(measurement: Measurement): Flow<List<MeasurementValue>>

    fun update(
        id: Long,
        updatedAt: DateTime,
        value: Double,
    )

    fun deleteById(id: Long)
}