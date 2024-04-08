package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.di.inject

class MeasurementValueSqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
    private val typeMapper: MeasurementTypeSqlDelightMapper = inject(),
    private val unitMapper: MeasurementUnitSqlDelightMapper = inject(),
    private val categoryMapper: MeasurementCategorySqlDelightMapper = inject(),
    private val entryMapper: EntrySqlDelightMapper = inject(),
) {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        value: Double,
        typeId: Long,
        entryId: Long,
    ): MeasurementValue {
        return MeasurementValue(
            id = id,
            createdAt = dateTimeFactory.dateTime(isoString = createdAt),
            updatedAt = dateTimeFactory.dateTime(isoString = updatedAt),
            value = value,
            typeId = typeId,
            entryId = entryId,
        )
    }

    fun map(
        valueId: Long,
        valueCreatedAt: String,
        valueUpdatedAt: String,
        valueValue: Double,
        valueTypeId: Long,
        valueEntryId: Long,

        typeId: Long,
        typeCreatedAt: String,
        typeUpdatedAt: String,
        typeKey: String?,
        typeName: String,
        typeSortIndex: Long,
        typeValueRangeMinimum: Double,
        typeValueRangeLow: Double?,
        typeValueRangeTarget: Double?,
        typeValueRangeHigh: Double?,
        typeValueRangeMaximum: Double,
        typeIsValueRangeHighlighted: Long,
        typeSelectedUnitId: Long,
        typeCategoryId: Long,

        unitId: Long,
        unitCreatedAt: String,
        unitUpdatedAt: String,
        unitKey: String?,
        unitName: String,
        unitAbbreviation: String,
        unitFactor: Double,
        unitTypeId: Long,

        categoryId: Long,
        categoryCreatedAt: String,
        categoryUpdatedAt: String,
        categoryKey: String?,
        categoryName: String,
        categoryIcon: String?,
        categorySortIndex: Long,

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
            typeId = valueTypeId,
            entryId = valueEntryId,
        ).apply {
            type = typeMapper.map(
                id = typeId,
                createdAt = typeCreatedAt,
                updatedAt = typeUpdatedAt,
                key = typeKey,
                name = typeName,
                sortIndex = typeSortIndex,
                valueRangeMinimum = typeValueRangeMinimum,
                valueRangeLow = typeValueRangeLow,
                valueRangeTarget = typeValueRangeTarget,
                valueRangeHigh = typeValueRangeHigh,
                valueRangeMaximum = typeValueRangeMaximum,
                isValueRangeHighlighted = typeIsValueRangeHighlighted,
                selectedUnitId = typeSelectedUnitId,
                categoryId = typeCategoryId,
            ).apply {
                category = categoryMapper.map(
                    id = categoryId,
                    createdAt = categoryCreatedAt,
                    updatedAt = categoryUpdatedAt,
                    key = categoryKey,
                    name = categoryName,
                    icon = categoryIcon,
                    sortIndex = categorySortIndex,
                )
                selectedUnit = unitMapper.map(
                    id = unitId,
                    createdAt = unitCreatedAt,
                    updatedAt = unitUpdatedAt,
                    key = unitKey,
                    name = unitName,
                    abbreviation = unitAbbreviation,
                    factor = unitFactor,
                    typeId = unitTypeId,
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