package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
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
        selectedUnitId: Long?,
        propertyId: Long,
    ): MeasurementType {
        return MeasurementType(
            id = id,
            createdAt = dateTimeFactory.dateTime(isoString = createdAt),
            updatedAt = dateTimeFactory.dateTime(isoString = updatedAt),
            key = key?.let(DatabaseKey.MeasurementType::from),
            name = name,
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
        typeSortIndex: Long,
        typeSelectedUnitId: Long?,
        typePropertyId: Long,
        selectedUnitId: Long,
        selectedUnitCreatedAt: String,
        selectedUnitUpdatedAt: String,
        selectedUnitKey: String?,
        selectedUnitName: String,
        selectedUnitFactor: Double,
        selectedUnitTypeId: Long,
    ): MeasurementType {
        return map(
            id = typeId,
            createdAt = typeCreatedAt,
            updatedAt = typeUpdatedAt,
            key = typeKey,
            name = typeName,
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
                factor = selectedUnitFactor,
                typeId = selectedUnitTypeId,
            )
        }
    }
}