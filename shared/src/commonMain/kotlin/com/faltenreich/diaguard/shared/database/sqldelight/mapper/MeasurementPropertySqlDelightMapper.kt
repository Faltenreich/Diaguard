package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory

class MeasurementPropertySqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
) {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        name: String,
        key: String?,
        icon: String?,
        sortIndex: Long,
    ): MeasurementProperty {
        return MeasurementProperty(
            id = id,
            createdAt = dateTimeFactory.dateTime(isoString = createdAt),
            updatedAt = dateTimeFactory.dateTime(isoString = updatedAt),
            name = name,
            key = key,
            icon = icon,
            sortIndex = sortIndex,
        )
    }
}