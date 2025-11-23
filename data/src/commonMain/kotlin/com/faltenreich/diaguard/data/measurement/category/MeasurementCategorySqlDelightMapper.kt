package com.faltenreich.diaguard.data.measurement.category

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.data.sqldelight.toSqlLiteBoolean

internal class MeasurementCategorySqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
) {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        key: String?,
        name: String,
        icon: String?,
        sortIndex: Long,
        isActive: Long,
    ): MeasurementCategory.Local {
        return MeasurementCategory.Local(
            id = id,
            createdAt = dateTimeFactory.dateTime(isoString = createdAt),
            updatedAt = dateTimeFactory.dateTime(isoString = updatedAt),
            key = key?.let(DatabaseKey.MeasurementCategory::from),
            name = name,
            icon = icon,
            sortIndex = sortIndex,
            isActive = isActive.toSqlLiteBoolean(),
        )
    }
}