package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.di.inject

class MeasurementValueSqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
    private val propertyMapper: MeasurementPropertySqlDelightMapper = inject(),
    private val unitMapper: MeasurementUnitSqlDelightMapper = inject(),
    private val categoryMapper: MeasurementCategorySqlDelightMapper = inject(),
    private val entryMapper: EntrySqlDelightMapper = inject(),
) {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        value: Double,
        propertyId: Long,
        entryId: Long,
    ): MeasurementValue {
        return MeasurementValue(
            id = id,
            createdAt = dateTimeFactory.dateTime(isoString = createdAt),
            updatedAt = dateTimeFactory.dateTime(isoString = updatedAt),
            value = value,
            propertyId = propertyId,
            entryId = entryId,
        )
    }

    fun map(
        valueId: Long,
        valueCreatedAt: String,
        valueUpdatedAt: String,
        valueValue: Double,
        valuePropertyId: Long,
        valueEntryId: Long,

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
        propertySelectedUnitId: Long,
        propertyCategoryId: Long,

        unitId: Long,
        unitCreatedAt: String,
        unitUpdatedAt: String,
        unitKey: String?,
        unitName: String,
        unitAbbreviation: String,
        unitFactor: Double,
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
    ): MeasurementValue {
        return MeasurementValue(
            id = valueId,
            createdAt = dateTimeFactory.dateTime(isoString = valueCreatedAt),
            updatedAt = dateTimeFactory.dateTime(isoString = valueUpdatedAt),
            value = valueValue,
            propertyId = valuePropertyId,
            entryId = valueEntryId,
        ).apply {
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
                selectedUnitId = propertySelectedUnitId,
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
                selectedUnit = unitMapper.map(
                    id = unitId,
                    createdAt = unitCreatedAt,
                    updatedAt = unitUpdatedAt,
                    key = unitKey,
                    name = unitName,
                    abbreviation = unitAbbreviation,
                    factor = unitFactor,
                    propertyId = unitPropertyId,
                )
            }
            entry = entryMapper.map(
                id = entryId,
                createdAt = entryCreatedAt,
                updatedAt = entryUpdatedAt,
                dateTime = entryDateTime,
                note = entryNote,
            )
        }
    }
}