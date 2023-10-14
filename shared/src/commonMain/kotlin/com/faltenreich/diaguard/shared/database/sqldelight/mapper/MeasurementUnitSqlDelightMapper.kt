package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory

class MeasurementUnitSqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
) {

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
            createdAt = dateTimeFactory.dateTime(isoString = createdAt),
            updatedAt = dateTimeFactory.dateTime(isoString = updatedAt),
            name = name,
            factor = factor,
            typeId = typeId,
        )
    }
}