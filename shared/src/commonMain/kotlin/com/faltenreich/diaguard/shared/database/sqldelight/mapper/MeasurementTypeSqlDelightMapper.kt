package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightExtensions.toSqlLiteBoolean
import com.faltenreich.diaguard.shared.di.inject

class MeasurementTypeSqlDelightMapper(
    private val dateTimeFactory: DateTimeFactory,
    private val unitMapper: MeasurementUnitSqlDelightMapper = inject(),
) {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        key: String?,
        name: String,
        sortIndex: Long,
        valueRangeMinimum: Double,
        valueRangeLow: Double?,
        valueRangeTarget: Double?,
        valueRangeHigh: Double?,
        valueRangeMaximum: Double,
        isValueRangeHighlighted: Long,
        selectedUnitId: Long,
        categoryId: Long,
    ): MeasurementType {
        return MeasurementType(
            id = id,
            createdAt = dateTimeFactory.dateTime(isoString = createdAt),
            updatedAt = dateTimeFactory.dateTime(isoString = updatedAt),
            key = key?.let(DatabaseKey.MeasurementType::from),
            name = name,
            range = MeasurementValueRange(
                minimum = valueRangeMinimum,
                low = valueRangeLow,
                target = valueRangeTarget,
                high = valueRangeHigh,
                maximum = valueRangeMaximum,
                isHighlighted = isValueRangeHighlighted.toSqlLiteBoolean(),
            ),
            sortIndex = sortIndex,
            selectedUnitId = selectedUnitId,
            categoryId = categoryId,
        )
    }

    fun map(
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

        selectedUnitId: Long,
        selectedUnitCreatedAt: String,
        selectedUnitUpdatedAt: String,
        selectedUnitKey: String?,
        selectedUnitName: String,
        selectedUnitAbbreviation: String,
        selectedUnitFactor: Double,
        selectedUnitTypeId: Long,
    ): MeasurementType {
        return map(
            id = typeId,
            createdAt = typeCreatedAt,
            updatedAt = typeUpdatedAt,
            key = typeKey,
            name = typeName,
            valueRangeMinimum = typeValueRangeMinimum,
            valueRangeLow = typeValueRangeLow,
            valueRangeTarget = typeValueRangeTarget,
            valueRangeHigh = typeValueRangeHigh,
            valueRangeMaximum = typeValueRangeMaximum,
            isValueRangeHighlighted = typeIsValueRangeHighlighted,
            sortIndex = typeSortIndex,
            selectedUnitId = typeSelectedUnitId,
            categoryId = typeCategoryId,
        ).apply {
            selectedUnit = unitMapper.map(
                id = selectedUnitId,
                createdAt = selectedUnitCreatedAt,
                updatedAt = selectedUnitUpdatedAt,
                key = selectedUnitKey,
                name = selectedUnitName,
                abbreviation = selectedUnitAbbreviation,
                factor = selectedUnitFactor,
                typeId = selectedUnitTypeId,
            )
        }
    }
}