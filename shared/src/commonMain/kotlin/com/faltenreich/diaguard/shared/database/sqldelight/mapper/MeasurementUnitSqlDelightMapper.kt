package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.database.DatabaseKey

class MeasurementUnitSqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
) {

    fun map(
        unitId: Long,
        unitCreatedAt: String,
        unitUpdatedAt: String,
        unitKey: String?,
        unitName: String,
        unitAbbreviation: String,
    ): MeasurementUnit.Local {
        return MeasurementUnit.Local(
            id = unitId,
            createdAt = dateTimeFactory.dateTime(isoString = unitCreatedAt),
            updatedAt = dateTimeFactory.dateTime(isoString = unitUpdatedAt),
            key = unitKey?.let(DatabaseKey.MeasurementUnit::from),
            name = unitName,
            abbreviation = unitAbbreviation,
        )
    }
}