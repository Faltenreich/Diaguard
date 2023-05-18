package com.faltenreich.diaguard.shared.database.sqldelight.dao

import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.DateTime

class MeasurementValueSqlDelightMapper {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        value: Double,
        typeId: Long,
        measurementId: Long,
    ): MeasurementValue {
        return MeasurementValue(
            id = id,
            createdAt = DateTime(isoString = createdAt),
            updatedAt = DateTime(isoString = updatedAt),
            value = value,
            typeId = typeId,
            measurementId = measurementId,
        )
    }
}