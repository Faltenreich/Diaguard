package com.faltenreich.diaguard.measurement.unit.form

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository

class GetMeasurementUnitUseCase(private val repository: MeasurementUnitRepository) {

    operator fun invoke(id: Long): MeasurementUnit.Local? {
        return repository.getById(id)
    }
}