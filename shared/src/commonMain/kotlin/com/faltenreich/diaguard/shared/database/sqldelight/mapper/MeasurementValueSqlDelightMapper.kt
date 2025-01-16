package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.value.MeasurementValue

class MeasurementValueSqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
    private val propertyMapper: MeasurementPropertySqlDelightMapper,
    private val entryMapper: EntrySqlDelightMapper,
) {

    fun map(
        valueId: Long,
        valueCreatedAt: String,
        valueUpdatedAt: String,
        valueValue: Double,
        @Suppress("UNUSED_PARAMETER") valuePropertyId: Long,
        @Suppress("UNUSED_PARAMETER") valueEntryId: Long,

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

        entryId: Long,
        entryCreatedAt: String,
        entryUpdatedAt: String,
        entryDateTime: String,
        entryNote: String?,
    ): MeasurementValue.Local {
        return MeasurementValue.Local(
            id = valueId,
            createdAt = dateTimeFactory.dateTime(isoString = valueCreatedAt),
            updatedAt = dateTimeFactory.dateTime(isoString = valueUpdatedAt),
            value = valueValue,
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
            entry = entryMapper.map(
                id = entryId,
                createdAt = entryCreatedAt,
                updatedAt = entryUpdatedAt,
                dateTime = entryDateTime,
                note = entryNote,
            ),
        )
    }

    fun map(
        average: Double?,

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
    ): MeasurementValue.Average {
        // TODO: Handle null
        average ?: throw IllegalStateException()

        return MeasurementValue.Average(
            value = average,
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