package com.faltenreich.diaguard.measurement.value

class MeasurementValueConverter {

    private fun convertToCustom(value: Double, factor: Double): Double {
        return value * factor
    }

    fun convertToCustom(value: IntermediateValue): Double {
        return convertToCustom(value = value.value, factor = value.unit.factor)
    }

    fun convertToCustom(value: MeasurementValue): Double {
        return convertToCustom(value = value.value, factor = value.type.selectedUnit.factor)
    }

    private fun convertToDefault(value: Double, factor: Double): Double {
        return value / factor
    }

    fun convertToDefault(value: InputValue): Double? {
        val number = value.value.toDoubleOrNull() ?: return null
        return convertToDefault(value = number, factor = value.unit.factor)
    }
}