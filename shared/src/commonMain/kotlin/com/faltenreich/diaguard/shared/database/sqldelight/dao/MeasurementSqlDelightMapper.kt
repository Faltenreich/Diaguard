package com.faltenreich.diaguard.shared.database.sqldelight.dao

import com.faltenreich.diaguard.measurement.Measurement
import com.faltenreich.diaguard.shared.datetime.DateTime

class MeasurementSqlDelightMapper {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        typeId: Long,
        entryId: Long,
    ): Measurement {
        return Measurement(
            id = id,
            createdAt = DateTime(isoString = createdAt),
            updatedAt = DateTime(isoString = updatedAt),
            typeId = typeId,
            entryId = entryId,
        )
    }
}