package com.faltenreich.diaguard.measurement.value.usecase

import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueTint

class GetMeasurementValueTintUseCase {

    operator fun invoke(measurementValue: MeasurementValue): MeasurementValueTint {
        return when {
            measurementValue.isNotHighlighted -> MeasurementValueTint.NONE
            measurementValue.isTooLow -> MeasurementValueTint.LOW
            measurementValue.isTooHigh -> MeasurementValueTint.HIGH
            else -> MeasurementValueTint.NORMAL
        }
    }
}