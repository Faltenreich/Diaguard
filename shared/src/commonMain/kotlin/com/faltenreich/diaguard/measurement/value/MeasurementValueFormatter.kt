package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.measurement.unit.MeasurementTypeUnit

class MeasurementValueFormatter {

    fun formatValue(value: MeasurementValue, unit: MeasurementTypeUnit): String {
        val valueForSelectedTypeUnit = value.value * unit.factor
        // TODO: Format separator according to language
        return valueForSelectedTypeUnit.toString()
    }
}