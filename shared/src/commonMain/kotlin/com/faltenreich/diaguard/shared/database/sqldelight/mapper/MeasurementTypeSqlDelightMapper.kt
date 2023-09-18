package com.faltenreich.diaguard.shared.database.sqldelight.mapper

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.di.inject

class MeasurementTypeSqlDelightMapper(
    private val unitMapper: MeasurementUnitSqlDelightMapper = inject(),
) {

    fun map(
        id: Long,
        createdAt: String,
        updatedAt: String,
        name: String,
        sortIndex: Long,
        selectedUnitId: Long?,
        propertyId: Long,
    ): MeasurementType {
        return MeasurementType(
            id = id,
            createdAt = DateTime(isoString = createdAt),
            updatedAt = DateTime(isoString = updatedAt),
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
        typeName: String,
        typeSortIndex: Long,
        typeSelectedUnitId: Long?,
        typePropertyId: Long,
        selectedUnitId: Long,
        selectedUnitCreatedAt: String,
        selectedUnitUpdatedAt: String,
        selectedUnitName: String,
        selectedUnitFactor: Double,
        selectedUnitTypeId: Long,
    ): MeasurementType {
        return map(
            id = typeId,
            createdAt = typeCreatedAt,
            updatedAt = typeUpdatedAt,
            name = typeName,
            sortIndex = typeSortIndex,
            selectedUnitId = typeSelectedUnitId,
            propertyId = typePropertyId,
        ).apply {
            selectedUnit = unitMapper.map(
                id = selectedUnitId,
                createdAt = selectedUnitCreatedAt,
                updatedAt = selectedUnitUpdatedAt,
                name = selectedUnitName,
                factor = selectedUnitFactor,
                typeId = selectedUnitTypeId,
            )
        }
    }
}