package com.faltenreich.diaguard.shared.database.sqldelight.dao

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.datetime.DateTime

class MeasurementTypeSqlDelightMapper {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        name: String,
        sortIndex: Long,
        selectedUnitId: Long,
        propertyId: Long,
    ): MeasurementType {
        return MeasurementType(
            id = id,
            createdAt = DateTime(isoString = createdAt),
            updatedAt = DateTime(isoString = updatedAt),
            name = name,
            sortIndex = sortIndex,
            selectedUnitId = selectedUnitId,
            propertyId = propertyId,
        )
    }
}