package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightExtensions.toSqlLiteBoolean

class MeasurementPropertySqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
    private val unitMapper: MeasurementUnitSqlDelightMapper,
    private val categoryMapper: MeasurementCategorySqlDelightMapper,
) {

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
        propertyCategoryId: Long,

        unitId: Long,
        unitCreatedAt: String,
        unitUpdatedAt: String,
        unitKey: String?,
        unitName: String,
        unitAbbreviation: String,
        unitFactor: Double,
        unitIsSelected: Long,
        unitPropertyId: Long,

        categoryId: Long,
        categoryCreatedAt: String,
        categoryUpdatedAt: String,
        categoryKey: String?,
        categoryName: String,
        categoryIcon: String?,
        categorySortIndex: Long,
        categoryIsActive: Long,
    ): MeasurementProperty.Local {
        return MeasurementProperty.Local(
            id = propertyId,
            createdAt = dateTimeFactory.dateTime(isoString = propertyCreatedAt),
            updatedAt = dateTimeFactory.dateTime(isoString = propertyUpdatedAt),
            key = propertyKey?.let(DatabaseKey.MeasurementProperty::from),
            name = propertyName,
            sortIndex = propertySortIndex,
            aggregationStyle = MeasurementAggregationStyle.fromStableId(propertyAggregationStyle.toInt()),
            range = MeasurementValueRange(
                minimum = propertyValueRangeMinimum,
                low = propertyValueRangeLow,
                target = propertyValueRangeTarget,
                high = propertyValueRangeHigh,
                maximum = propertyValueRangeMaximum,
                isHighlighted = propertyIsValueRangeHighlighted.toSqlLiteBoolean(),
            ),
            category = categoryMapper.map(
                id = categoryId,
                createdAt = categoryCreatedAt,
                updatedAt = categoryUpdatedAt,
                key = categoryKey,
                name = categoryName,
                icon = categoryIcon,
                sortIndex = categorySortIndex,
                isActive = categoryIsActive,
            ),
            selectedUnit = unitMapper.map(
                unitId = unitId,
                unitCreatedAt = unitCreatedAt,
                unitUpdatedAt = unitUpdatedAt,
                unitKey = unitKey,
                unitName = unitName,
                unitAbbreviation = unitAbbreviation,
                unitFactor = unitFactor,
                unitIsSelected = unitIsSelected,
                unitPropertyId = unitPropertyId,

                propertyId = propertyId,
                propertyCreatedAt = propertyCreatedAt,
                propertyUpdatedAt = propertyUpdatedAt,
                propertyKey = propertyKey,
                propertyName = propertyName,
                propertySortIndex = propertySortIndex,
                propertyAggregationStyle = propertyAggregationStyle,
                propertyValueRangeMinimum = propertyValueRangeMinimum,
                propertyValueRangeLow = propertyValueRangeLow,
                propertyValueRangeTarget = propertyValueRangeTarget,
                propertyValueRangeHigh = propertyValueRangeHigh,
                propertyValueRangeMaximum= propertyValueRangeMaximum,
                propertyIsValueRangeHighlighted = propertyIsValueRangeHighlighted,
                propertyCategoryId = propertyCategoryId,

                categoryId = categoryId,
                categoryCreatedAt = categoryCreatedAt,
                categoryUpdatedAt = categoryUpdatedAt,
                categoryKey = categoryKey,
                categoryName = categoryName,
                categoryIcon = categoryIcon,
                categorySortIndex = categorySortIndex,
                categoryIsActive = categoryIsActive,
            )
        )
    }
}