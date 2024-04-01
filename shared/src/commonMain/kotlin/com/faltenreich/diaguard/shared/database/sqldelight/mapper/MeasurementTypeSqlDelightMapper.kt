package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.database.DatabaseKey
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
        minimumValue: Double,
        lowValue: Double?,
        targetValue: Double?,
        highValue: Double?,
        maximumValue: Double,
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
            minimumValue = minimumValue,
            lowValue = lowValue,
            targetValue = targetValue,
            highValue = highValue,
            maximumValue = maximumValue,
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
        typeMinimumValue: Double,
        typeLowValue: Double?,
        typeTargetValue: Double?,
        typeHighValue: Double?,
        typeMaximumValue: Double,
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
            minimumValue = typeMinimumValue,
            lowValue = typeLowValue,
            targetValue = typeTargetValue,
            highValue = typeHighValue,
            maximumValue = typeMaximumValue,
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