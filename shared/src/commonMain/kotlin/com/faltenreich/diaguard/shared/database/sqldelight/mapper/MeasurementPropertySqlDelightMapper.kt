package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightExtensions.toSqlLiteBoolean

class MeasurementPropertySqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
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
        @Suppress("UNUSED_PARAMETER") propertyCategoryId: Long,
        @Suppress("UNUSED_PARAMETER") propertyUnitId: Long,

        unitId: Long,
        unitCreatedAt: String,
        unitUpdatedAt: String,
        unitKey: String?,
        unitName: String,
        unitAbbreviation: String,
        unitFactor: Double,
        unitIsSelected: Long,
        @Suppress("UNUSED_PARAMETER") unitPropertyId: Long,

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
        ).apply {
            // We avoid recursion by initializing late and without mapper
            unit = MeasurementUnit.Local(
                id = unitId,
                createdAt = dateTimeFactory.dateTime(isoString = unitCreatedAt),
                updatedAt = dateTimeFactory.dateTime(isoString = unitUpdatedAt),
                key = unitKey?.let(DatabaseKey.MeasurementUnit::from),
                name = unitName,
                abbreviation = unitAbbreviation,
                factor = unitFactor,
                isSelected = unitIsSelected.toSqlLiteBoolean(),
                property = this,
            )
        }
    }
}