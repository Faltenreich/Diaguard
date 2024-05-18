package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.di.inject

class MeasurementValueSqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
    private val propertyMapper: MeasurementPropertySqlDelightMapper = inject(),
    private val categoryMapper: MeasurementCategorySqlDelightMapper = inject(),
    private val entryMapper: EntrySqlDelightMapper = inject(),
) {

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
        propertyCategoryId: Long,

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
            entry = entryMapper.map(
                id = entryId,
                createdAt = entryCreatedAt,
                updatedAt = entryUpdatedAt,
                dateTime = entryDateTime,
                note = entryNote,
            ),
        )
    }
}