package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface MeasurementUnitDao {

    fun create(
        createdAt: DateTime,
        name: String,
        factor: Double,
        type: MeasurementType,
    )

    fun getLastId(): Long?

    fun getByType(type: MeasurementType): Flow<List<MeasurementUnit>>

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        factor: Double,
    )

    fun deleteById(id: Long)
}