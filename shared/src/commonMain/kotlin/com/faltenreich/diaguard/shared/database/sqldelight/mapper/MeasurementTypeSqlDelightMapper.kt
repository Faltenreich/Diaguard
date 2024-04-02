package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.value.MeasurementValueRange
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
        minimumRangeValue: Double,
        lowRangeValue: Double?,
        targetRangeValue: Double?,
        highRangeValue: Double?,
        maximumRangeValue: Double,
        isRangeHighlighted: Long,
        sortIndex: Long,
        selectedUnitId: Long,
        propertyId: Long,
    ): MeasurementType {
        return MeasurementType(
            id = id,
            createdAt = dateTimeFactory.dateTime(isoString = createdAt),
            updatedAt = dateTimeFactory.dateTime(isoString = updatedAt),
            key = key?.let(DatabaseKey.MeasurementType::from),
            name = name,
            range = MeasurementValueRange(
                minimum = minimumRangeValue,
                low = lowRangeValue,
                target = targetRangeValue,
                high = highRangeValue,
                maximum = maximumRangeValue,
                isHighlighted = isRangeHighlighted.toSqlLiteBoolean(),
            ),
            sortIndex = sortIndex,
            selectedUnitId = selectedUnitId,
            propertyId = propertyId,
        )
    }

    fun map(
        typeId: Long,
        typeCreatedAt: String,
        typeUpdatedAt: String,
        typeKey: String?,
        typeName: String,
        typeMinimumRangeValue: Double,
        typeLowRangeValue: Double?,
        typeTargetRangeValue: Double?,
        typeHighRangeValue: Double?,
        typeMaximumRangeValue: Double,
        typeIsRangeHighlighted: Long,
        typeSortIndex: Long,
        typeSelectedUnitId: Long,
        typePropertyId: Long,

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
            minimumRangeValue = typeMinimumRangeValue,
            lowRangeValue = typeLowRangeValue,
            targetRangeValue = typeTargetRangeValue,
            highRangeValue = typeHighRangeValue,
            maximumRangeValue = typeMaximumRangeValue,
            isRangeHighlighted = typeIsRangeHighlighted,
            sortIndex = typeSortIndex,
            selectedUnitId = typeSelectedUnitId,
            propertyId = typePropertyId,
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