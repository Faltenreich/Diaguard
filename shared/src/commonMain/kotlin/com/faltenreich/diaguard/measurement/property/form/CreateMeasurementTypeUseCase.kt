package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.di.inject

class CreateMeasurementTypeUseCase(
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
    private val measurementUnitRepository: MeasurementUnitRepository = inject(),
) {

    operator fun invoke(
        typeName: String,
        typeKey: String?,
        typeSortIndex: Long,
        propertyId: Long,
        unitName: String,
    ) {
        val typeId = measurementTypeRepository.create(
            name = typeName,
            key = typeKey,
            sortIndex = typeSortIndex,
            propertyId = propertyId,
        )
        val unitId = measurementUnitRepository.create(
            name = unitName,
            factor = 1.0,
            typeId = typeId,
        )
        measurementTypeRepository.update(
            id = typeId,
            name = typeName,
            sortIndex = typeSortIndex,
            selectedUnitId = unitId,
        )
    }
}