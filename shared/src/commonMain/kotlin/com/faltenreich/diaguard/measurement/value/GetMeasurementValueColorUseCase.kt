package com.faltenreich.diaguard.measurement.value

class GetMeasurementValueColorUseCase {

    operator fun invoke(measurementValue: MeasurementValue): MeasurementValueColor {
        return when {
            // TODO: Return MeasurementValueColor.DEFAULT if user setting is disabled
            measurementValue.isTooLow -> MeasurementValueColor.LOW
            measurementValue.isTooHigh -> MeasurementValueColor.HIGH
            else -> MeasurementValueColor.NORMAL
        }
    }
}