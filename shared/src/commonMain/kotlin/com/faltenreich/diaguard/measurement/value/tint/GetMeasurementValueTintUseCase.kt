package com.faltenreich.diaguard.measurement.value.tint

import com.faltenreich.diaguard.measurement.value.MeasurementValue

class GetMeasurementValueTintUseCase {

    operator fun invoke(measurementValue: MeasurementValue): MeasurementValueTint {
        return when {
            // TODO: Return MeasurementValueColor.DEFAULT if user setting is disabled
            measurementValue.isTooLow -> MeasurementValueTint.LOW
            measurementValue.isTooHigh -> MeasurementValueTint.HIGH
            else -> MeasurementValueTint.NORMAL
        }
    }
}