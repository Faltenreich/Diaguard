package com.faltenreich.diaguard.data.measurement.value

class MeasurementValueTintMapper {

    operator fun invoke(measurementValue: MeasurementValue): MeasurementValueTint {
        return when {
            measurementValue.isNotHighlighted -> MeasurementValueTint.NONE
            measurementValue.isTooLow -> MeasurementValueTint.LOW
            measurementValue.isTooHigh -> MeasurementValueTint.HIGH
            else -> MeasurementValueTint.NORMAL
        }
    }
}