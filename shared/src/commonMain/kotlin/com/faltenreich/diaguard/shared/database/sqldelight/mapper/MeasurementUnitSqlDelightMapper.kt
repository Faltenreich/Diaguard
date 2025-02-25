package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightExtensions.toSqlLiteBoolean

class MeasurementUnitSqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
    private val propertyMapper: MeasurementPropertySqlDelightMapper,
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
                propertyValueRangeMaximum = propertyValueRangeMaximum,
                propertyIsValueRangeHighlighted = propertyIsValueRangeHighlighted,
                propertyCategoryId = propertyCategoryId,

                unitId = unitId,
                unitCreatedAt = unitCreatedAt,
                unitUpdatedAt = unitUpdatedAt,
                unitKey = unitKey,
                unitName = unitName,
                unitAbbreviation = unitAbbreviation,
                unitFactor = unitFactor,
                unitIsSelected = unitIsSelected,
                unitPropertyId = unitPropertyId,

                categoryId = categoryId,
                categoryCreatedAt = categoryCreatedAt,
                categoryUpdatedAt = categoryUpdatedAt,
                categoryKey = categoryKey,
                categoryName = categoryName,
                categoryIcon = categoryIcon,
                categorySortIndex = categorySortIndex,
                categoryIsActive = categoryIsActive,
            ),
        )
    }
}