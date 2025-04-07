package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightExtensions.toSqlLiteBoolean

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
        unitFactor: Double,
        unitIsSelected: Long,
    ): MeasurementUnit.Local {
        return MeasurementUnit.Local(
            id = unitId,
            createdAt = dateTimeFactory.dateTime(isoString = unitCreatedAt),
            updatedAt = dateTimeFactory.dateTime(isoString = unitUpdatedAt),
            key = unitKey?.let(DatabaseKey.MeasurementUnit::from),
            name = unitName,
            abbreviation = unitAbbreviation,
            factor = unitFactor,
            isSelected = unitIsSelected.toSqlLiteBoolean(),
        )
    }
}