package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyUnit

class MeasurementPropertyUnitSqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
    private val propertyMapper: MeasurementPropertySqlDelightMapper,
    private val unitMapper: MeasurementUnitSqlDelightMapper,
) {

    fun map(
        propertyUnitId1: Long,
        propertyUnitCreatedAt: String,
        propertyUnitUpdatedAt: String,
        @Suppress("UNUSED_PARAMETER") propertyUnitPropertyId: Long,
        @Suppress("UNUSED_PARAMETER") propertyUnitUnitId: Long,

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
        propertyUnitId: Long,

        categoryId: Long,
        categoryCreatedAt: String,
        categoryUpdatedAt: String,
        categoryKey: String?,
        categoryName: String,
        categoryIcon: String?,
        categorySortIndex: Long,
        categoryIsActive: Long,

        unitId: Long,
        unitCreatedAt: String,
        unitUpdatedAt: String,
        unitKey: String?,
        unitName: String,
        unitAbbreviation: String,
        unitFactor: Double,
        unitIsSelected: Long,
    ): MeasurementPropertyUnit.Local {
        return MeasurementPropertyUnit.Local(
            id = propertyUnitId1,
            createdAt = dateTimeFactory.dateTime(isoString = propertyUnitCreatedAt),
            updatedAt = dateTimeFactory.dateTime(isoString = propertyUnitUpdatedAt),
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
                propertyUnitId = propertyUnitId,

                unitId = unitId,
                unitCreatedAt = unitCreatedAt,
                unitUpdatedAt = unitUpdatedAt,
                unitKey = unitKey,
                unitName = unitName,
                unitAbbreviation = unitAbbreviation,
                unitFactor = unitFactor,
                unitIsSelected = unitIsSelected,

                categoryId = categoryId,
                categoryCreatedAt = categoryCreatedAt,
                categoryUpdatedAt = categoryUpdatedAt,
                categoryKey = categoryKey,
                categoryName = categoryName,
                categoryIcon = categoryIcon,
                categorySortIndex = categorySortIndex,
                categoryIsActive = categoryIsActive,
            ),
            unit = unitMapper.map(
                unitId = unitId,
                unitCreatedAt = unitCreatedAt,
                unitUpdatedAt = unitUpdatedAt,
                unitKey = unitKey,
                unitName = unitName,
                unitAbbreviation = unitAbbreviation,
                unitFactor = unitFactor,
                unitIsSelected = unitIsSelected,
            ),
        )
    }
}