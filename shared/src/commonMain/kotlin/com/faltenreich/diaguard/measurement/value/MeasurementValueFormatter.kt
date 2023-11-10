package com.faltenreich.diaguard.measurement.value

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
}