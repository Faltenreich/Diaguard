package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.measurement.type.MeasurementType

class MeasurementValueConverter {

    fun convertToCustom(value: Double, factor: Double): Double {
        return value * factor
    }

    fun convertToCustom(value: MeasurementValue): Double {
        return convertToCustom(value = value.value, factor = value.type.selectedUnit.factor)
    }

    fun convertToCustom(value: Double, type: MeasurementType): Double {
        return convertToCustom(value = value, factor = type.selectedUnit.factor)
    }

    fun convertToDefault(value: Double, type: MeasurementType): Double {
        return value / type.selectedUnit.factor
    }
}