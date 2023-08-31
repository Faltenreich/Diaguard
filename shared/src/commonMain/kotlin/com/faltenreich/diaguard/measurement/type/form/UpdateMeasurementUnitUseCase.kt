package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.di.inject

class UpdateMeasurementUnitUseCase(
    private val measurementUnitRepository: MeasurementUnitRepository = inject(),
) {

    operator fun invoke(unit: MeasurementUnit) = with(unit) {
        measurementUnitRepository.update(
            id = id,
            updatedAt = DateTime.now(),
            name = name,
        )
    }
}