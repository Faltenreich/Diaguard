package com.faltenreich.diaguard.measurement.value

class MeasurementValueFormatter {

    fun formatValue(value: Double, factor: Double): String {
        val valueForSelectedUnit = value * factor
        // TODO: Format separator according to language
        return valueForSelectedUnit.toString()
    }

    fun formatValue(value: MeasurementValue): String {
        val selectedUnit = value.type.selectedUnit ?: throw IllegalStateException("Missing selected unit of type")
        return formatValue(value = value.value, factor = selectedUnit.factor)
    }
}