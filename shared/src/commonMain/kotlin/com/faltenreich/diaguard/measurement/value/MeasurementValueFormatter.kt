package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.primitive.NumberFormatter

class MeasurementValueFormatter(
    private val numberFormatter: NumberFormatter = inject(),
) {

    fun formatValue(value: Double, factor: Double = 1.0): String {
        return numberFormatter.format(value * factor)
    }

    fun formatValue(value: MeasurementValue): String {
        return formatValue(value = value.value, factor = value.type.selectedUnit.factor)
    }

    fun convertToDefault(value: Double, type: MeasurementType): Double {
        return value / type.selectedUnit.factor
    }

    fun convertToCustom(value: Double, type: MeasurementType): Double {
        return value * type.selectedUnit.factor
    }
}