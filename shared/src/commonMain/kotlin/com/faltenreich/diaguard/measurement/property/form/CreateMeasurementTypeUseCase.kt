package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange

class CreateMeasurementTypeUseCase(
    private val measurementTypeRepository: MeasurementTypeRepository,
    private val measurementUnitRepository: MeasurementUnitRepository,
) {

    operator fun invoke(
        typeKey: String?,
        typeName: String,
        typeRange: MeasurementValueRange,
        typeSortIndex: Long,
        propertyId: Long,
        unitKey: String?,
        unitName: String,
    ) {
        val typeId = measurementTypeRepository.create(
            key = typeKey,
            name = typeName,
            sortIndex = typeSortIndex,
            range = typeRange,
            propertyId = propertyId,
        )
        val unitId = measurementUnitRepository.create(
            key = unitKey,
            name = unitName,
            abbreviation = unitName, // TODO: Make customizable?
            factor = MeasurementUnit.FACTOR_DEFAULT,
            typeId = typeId,
        )
        measurementTypeRepository.update(
            id = typeId,
            name = typeName,
            range = typeRange,
            sortIndex = typeSortIndex,
            selectedUnitId = unitId,
        )
    }
}