package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface MeasurementValueDao {

    fun create(
        createdAt: DateTime,
        value: Double,
        typeId: Long,
        measurementId: Long,
    )

    fun getLastId(): Long?

    fun getByMeasurementId(measurementId: Long): Flow<List<MeasurementValue>>

    fun update(
        id: Long,
        updatedAt: DateTime,
        value: Double,
    )

    fun deleteById(id: Long)
}