package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightExtensions.toSqlLiteBoolean

class MeasurementUnitSqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
    private val propertyMapper: MeasurementPropertySqlDelightMapper,
    private val categoryMapper: MeasurementCategorySqlDelightMapper,
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
        unitPropertyId: Long,

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

        categoryId: Long,
        categoryCreatedAt: String,
        categoryUpdatedAt: String,
        categoryKey: String?,
        categoryName: String,
        categoryIcon: String?,
        categorySortIndex: Long,
        categoryIsActive: Long,
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
            property = propertyMapper.map(
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
                categoryId = propertyCategoryId,
            ).apply {
                category = categoryMapper.map(
                    id = categoryId,
                    createdAt = categoryCreatedAt,
                    updatedAt = categoryUpdatedAt,
                    key = categoryKey,
                    name = categoryName,
                    icon = categoryIcon,
                    sortIndex = categorySortIndex,
                    isActive = categoryIsActive,
                )
            },
        )
    }
}