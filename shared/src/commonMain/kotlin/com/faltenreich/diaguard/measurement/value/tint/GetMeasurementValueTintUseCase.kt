package com.faltenreich.diaguard.measurement.value.tint

import com.faltenreich.diaguard.measurement.value.MeasurementValue

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