package com.faltenreich.diaguard.shared.database.sqldelight.dao

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.datetime.DateTime

class MeasurementUnitSqlDelightMapper {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        name: String,
        factor: Double,
        typeId: Long,
    ): MeasurementUnit {
        return MeasurementUnit(
            id = id,
            createdAt = DateTime(isoString = createdAt),
            updatedAt = DateTime(isoString = updatedAt),
            name = name,
            factor = factor,
            typeId = typeId,
        )
    }
}