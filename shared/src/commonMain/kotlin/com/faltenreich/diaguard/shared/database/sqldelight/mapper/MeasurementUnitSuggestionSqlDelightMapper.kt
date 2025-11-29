package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestion

class MeasurementUnitSuggestionSqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
    private val unitMapper: MeasurementUnitSqlDelightMapper,
    private val propertyMapper: MeasurementPropertySqlDelightMapper,
) {

    fun map(
        unitSuggestionId: Long,
        unitSuggestionCreatedAt: String,
        unitSuggestionUpdatedAt: String,
        unitSuggestionFactor: Double,
        unitSuggestionPropertyId: Long,
        unitSuggestionUnitId: Long,

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
    ): MeasurementUnitSuggestion.Local {
        return MeasurementUnitSuggestion.Local(
            id = unitSuggestionId,
            createdAt = dateTimeFactory.dateTime(isoString = unitSuggestionCreatedAt),
            updatedAt = dateTimeFactory.dateTime(isoString = unitSuggestionUpdatedAt),
            factor = unitSuggestionFactor,
            unit = unitMapper.map(
                id = unitId,
                createdAt = unitCreatedAt,
                updatedAt = unitUpdatedAt,
                key = unitKey,
                name = unitName,
                abbreviation = unitAbbreviation,
            ),
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

                categoryId = categoryId,
                categoryCreatedAt = categoryCreatedAt,
                categoryUpdatedAt = categoryUpdatedAt,
                categoryKey = categoryKey,
                categoryName = categoryName,
                categoryIcon = categoryIcon,
                categorySortIndex = categorySortIndex,
                categoryIsActive = categoryIsActive,

                unitId = unitId,
                unitCreatedAt = unitCreatedAt,
                unitUpdatedAt = unitUpdatedAt,
                unitKey = unitKey,
                unitName = unitName,
                unitAbbreviation = unitAbbreviation,

                unitSuggestionId = unitSuggestionId,
                unitSuggestionCreatedAt = unitSuggestionCreatedAt,
                unitSuggestionUpdatedAt = unitSuggestionUpdatedAt,
                unitSuggestionFactor = unitSuggestionFactor,
                unitSuggestionPropertyId = unitSuggestionPropertyId,
                unitSuggestionUnitId = unitSuggestionUnitId,
            ),
        )
    }
}