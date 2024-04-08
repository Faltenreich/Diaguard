package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.shared.database.DatabaseKey

class MeasurementCategorySqlDelightMapper(
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
    ): MeasurementCategory {
        return MeasurementCategory(
            id = id,
            createdAt = dateTimeFactory.dateTime(isoString = createdAt),
            updatedAt = dateTimeFactory.dateTime(isoString = updatedAt),
            key = key?.let(DatabaseKey.MeasurementCategory::from),
            name = name,
            icon = icon,
            sortIndex = sortIndex,
        )
    }
}