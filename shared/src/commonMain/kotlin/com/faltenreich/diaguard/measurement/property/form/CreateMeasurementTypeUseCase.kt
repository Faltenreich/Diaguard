package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.di.inject

class CreateMeasurementTypeUseCase(
    private val measurementTypeRepository: MeasurementTypeRepository = inject(),
    private val measurementUnitRepository: MeasurementUnitRepository = inject(),
) {

    operator fun invoke(
        typeKey: String?,
        typeName: String,
        typeSortIndex: Long,
        propertyId: Long,
        unitKey: String?,
        unitName: String,
    ) {
        val typeId = measurementTypeRepository.create(
            name = typeName,
            key = typeKey,
            sortIndex = typeSortIndex,
            propertyId = propertyId,
        )
        val unitId = measurementUnitRepository.create(
            key = unitKey,
            name = unitName,
            factor = MeasurementUnit.FACTOR_DEFAULT,
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