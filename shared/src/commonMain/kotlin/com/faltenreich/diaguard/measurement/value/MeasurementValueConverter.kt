package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.shared.primitive.NumberFormatter

class MeasurementValueConverter(
    private val numberFormatter: NumberFormatter,
) {

    fun convertToCustom(value: MeasurementValue): MeasurementValueForUser {
        val unit = value.type.selectedUnit
        return MeasurementValueForUser(
            value = numberFormatter(value.value * unit.factor),
            unit = unit,
        )
    }

    fun convertToCustom(value: MeasurementValueForDatabase): MeasurementValueForUser {
        val unit = value.unit
        return MeasurementValueForUser(
            value = numberFormatter(value.value * unit.factor),
            unit = unit,
        )
    }

    fun convertToDefault(value: MeasurementValueForUser): MeasurementValueForDatabase? {
        val number = value.value.toDoubleOrNull() ?: return null
        val unit = value.unit
        return MeasurementValueForDatabase(value = number / unit.factor, unit = unit)
    }
}