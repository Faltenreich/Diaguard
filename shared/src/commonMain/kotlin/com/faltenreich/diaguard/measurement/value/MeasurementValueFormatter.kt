package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.primitive.NumberFormatter

class MeasurementValueFormatter(
    private val numberFormatter: NumberFormatter = inject(),
) {

    fun formatValue(value: Double, factor: Double): String {
        return numberFormatter.format(value * factor)
    }

    fun formatValue(value: MeasurementValue): String {
        val selectedUnit = value.type.selectedUnit ?: throw IllegalStateException("Missing selected unit of type")
        return formatValue(value = value.value, factor = selectedUnit.factor)
    }
}