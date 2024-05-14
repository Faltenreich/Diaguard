package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightExtensions.toSqlLiteBoolean
import com.faltenreich.diaguard.shared.di.inject

class MeasurementPropertySqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
    private val unitMapper: MeasurementUnitSqlDelightMapper = inject(),
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
        selectedUnitId: Long?,
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
            selectedUnitId = selectedUnitId,
            categoryId = categoryId,
        )
    }

    fun map(
        propertyId: Long,
        propertyCreatedAt: String,
        propertyUpdatedAt: String,
        propertyKey: String?,
        propertyName: String,
        propertySortIndex: Long,
        propertyAggregationStyle: Long,
        propertyValueRangeMinimum: Double,
        propertyValueRangeLow: Double?,
        propertyValueRangeTarget: Double?,
        propertyValueRangeHigh: Double?,
        propertyValueRangeMaximum: Double,
        propertyIsValueRangeHighlighted: Long,
        propertySelectedUnitId: Long?,
        propertyCategoryId: Long,

        selectedUnitId: Long,
        selectedUnitCreatedAt: String,
        selectedUnitUpdatedAt: String,
        selectedUnitKey: String?,
        selectedUnitName: String,
        selectedUnitAbbreviation: String,
        selectedUnitFactor: Double,
        selectedUnitPropertyId: Long,
    ): MeasurementProperty {
        return map(
            id = propertyId,
            createdAt = propertyCreatedAt,
            updatedAt = propertyUpdatedAt,
            key = propertyKey,
            name = propertyName,
            sortIndex = propertySortIndex,
            aggregationStyle = propertyAggregationStyle,
            valueRangeMinimum = propertyValueRangeMinimum,
            valueRangeLow = propertyValueRangeLow,
            valueRangeTarget = propertyValueRangeTarget,
            valueRangeHigh = propertyValueRangeHigh,
            valueRangeMaximum = propertyValueRangeMaximum,
            isValueRangeHighlighted = propertyIsValueRangeHighlighted,
            selectedUnitId = propertySelectedUnitId,
            categoryId = propertyCategoryId,
        ).apply {
            selectedUnit = unitMapper.map(
                id = selectedUnitId,
                createdAt = selectedUnitCreatedAt,
                updatedAt = selectedUnitUpdatedAt,
                key = selectedUnitKey,
                name = selectedUnitName,
                abbreviation = selectedUnitAbbreviation,
                factor = selectedUnitFactor,
                propertyId = selectedUnitPropertyId,
            )
        }
    }
}