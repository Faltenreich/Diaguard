package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightExtensions.toSqlLiteBoolean

class MeasurementPropertySqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
) {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        key: String?,
        name: String,
        sortIndex: Long,
        aggregationStyle: Long,
        valueRangeMinimum: Double,
        valueRangeLow: Double?,
        valueRangeTarget: Double?,
        valueRangeHigh: Double?,
        valueRangeMaximum: Double,
        isValueRangeHighlighted: Long,
        categoryId: Long,
    ): MeasurementProperty {
        return MeasurementProperty(
            id = id,
            createdAt = dateTimeFactory.dateTime(isoString = createdAt),
            updatedAt = dateTimeFactory.dateTime(isoString = updatedAt),
            key = key?.let(DatabaseKey.MeasurementProperty::from),
            name = name,
            aggregationStyle = MeasurementAggregationStyle.fromStableId(aggregationStyle.toInt()),
            range = MeasurementValueRange(
                minimum = valueRangeMinimum,
                low = valueRangeLow,
                target = valueRangeTarget,
                high = valueRangeHigh,
                maximum = valueRangeMaximum,
                isHighlighted = isValueRangeHighlighted.toSqlLiteBoolean(),
            ),
            sortIndex = sortIndex,
            categoryId = categoryId,
        )
    }
}