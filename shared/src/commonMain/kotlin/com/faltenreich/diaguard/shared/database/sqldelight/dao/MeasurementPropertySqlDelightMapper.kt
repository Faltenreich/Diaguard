package com.faltenreich.diaguard.shared.database.sqldelight.dao

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.shared.datetime.DateTime

class MeasurementPropertySqlDelightMapper {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        name: String,
        sortIndex: Long,
        selectedUnitId: Long,
    ): MeasurementProperty {
        return MeasurementProperty(
            id = id,
            createdAt = DateTime(isoString = createdAt),
            updatedAt = DateTime(isoString = updatedAt),
            name = name,
            sortIndex = sortIndex,
            selectedUnitId = selectedUnitId,
        )
    }
}