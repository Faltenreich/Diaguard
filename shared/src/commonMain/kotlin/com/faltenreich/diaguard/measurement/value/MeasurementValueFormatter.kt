package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

class MeasurementValueFormatter {

    fun formatValue(value: MeasurementValue, unit: MeasurementUnit): String {
        val valueForSelectedUnit = value.value * unit.factor
        // TODO: Format separator according to language
        return valueForSelectedUnit.toString()
    }
}