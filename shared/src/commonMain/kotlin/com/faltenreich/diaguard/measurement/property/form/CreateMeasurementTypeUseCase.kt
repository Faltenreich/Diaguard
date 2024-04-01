package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository

class CreateMeasurementTypeUseCase(
    private val measurementTypeRepository: MeasurementTypeRepository,
    private val measurementUnitRepository: MeasurementUnitRepository,
) {

    operator fun invoke(
        typeKey: String?,
        typeName: String,
        typeMinimumValue: Double,
        typeLowValue: Double?,
        typeTargetValue: Double?,
        typeHighValue: Double?,
        typeMaximumValue: Double,
        typeSortIndex: Long,
        propertyId: Long,
        unitKey: String?,
        unitName: String,
    ) {
        val typeId = measurementTypeRepository.create(
            key = typeKey,
            name = typeName,
            minimumValue = typeMinimumValue,
            lowValue = typeLowValue,
            targetValue = typeTargetValue,
            highValue = typeHighValue,
            maximumValue = typeMaximumValue,
            sortIndex = typeSortIndex,
            propertyId = propertyId,
        )
        val unitId = measurementUnitRepository.create(
            key = unitKey,
            name = unitName,
            abbreviation = unitName, // TODO: Make it customizable?
            factor = MeasurementUnit.FACTOR_DEFAULT,
            typeId = typeId,
        )
        measurementTypeRepository.update(
            id = typeId,
            name = typeName,
            minimumValue = typeMinimumValue,
            lowValue = typeLowValue,
            targetValue = typeTargetValue,
            highValue = typeHighValue,
            maximumValue = typeMaximumValue,
            sortIndex = typeSortIndex,
            selectedUnitId = unitId,
        )
    }
}