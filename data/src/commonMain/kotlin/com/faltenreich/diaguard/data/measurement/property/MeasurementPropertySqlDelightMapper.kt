package com.faltenreich.diaguard.data.measurement.property

import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategorySqlDelightMapper
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.data.measurement.unit.suggestion.MeasurementUnitSuggestion
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.persistence.sqldelight.SqlDelightExtensions.toSqlLiteBoolean

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

        @Suppress("UNUSED_PARAMETER") unitSuggestionId: Long?,
        @Suppress("UNUSED_PARAMETER") unitSuggestionCreatedAt: String?,
        @Suppress("UNUSED_PARAMETER") unitSuggestionUpdatedAt: String?,
        unitSuggestionFactor: Double?,
        @Suppress("UNUSED_PARAMETER") unitSuggestionPropertyId: Long?,
        @Suppress("UNUSED_PARAMETER") unitSuggestionUnitId: Long?,
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
            unit = MeasurementUnit.Local(
                id = unitId,
                createdAt = dateTimeFactory.dateTime(isoString = unitCreatedAt),
                updatedAt = dateTimeFactory.dateTime(isoString = unitUpdatedAt),
                key = unitKey?.let(DatabaseKey.MeasurementUnit::from),
                name = unitName,
                abbreviation = unitAbbreviation,
            ),
            valueFactor = unitSuggestionFactor ?: MeasurementUnitSuggestion.FACTOR_DEFAULT,
        )
    }
}