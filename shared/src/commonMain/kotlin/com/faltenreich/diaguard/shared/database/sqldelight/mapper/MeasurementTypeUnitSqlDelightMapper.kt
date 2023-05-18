package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.measurement.unit.MeasurementTypeUnit
import com.faltenreich.diaguard.shared.datetime.DateTime

class MeasurementTypeUnitSqlDelightMapper {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        factor: Double,
        typeId: Long,
        unitId: Long,
    ): MeasurementTypeUnit {
        return MeasurementTypeUnit(
            id = id,
            createdAt = DateTime(isoString = createdAt),
            updatedAt = DateTime(isoString = updatedAt),
            factor = factor,
            typeId = typeId,
            unitId = unitId,
        )
    }
}