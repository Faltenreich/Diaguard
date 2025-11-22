package com.faltenreich.diaguard.data.measurement.unit

import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory

class MeasurementUnitSqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
) {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        key: String?,
        name: String,
        abbreviation: String,
    ): MeasurementUnit.Local {
        return MeasurementUnit.Local(
            id = id,
            createdAt = dateTimeFactory.dateTime(isoString = createdAt),
            updatedAt = dateTimeFactory.dateTime(isoString = updatedAt),
            key = key?.let(DatabaseKey.MeasurementUnit::from),
            name = name,
            abbreviation = abbreviation,
        )
    }
}