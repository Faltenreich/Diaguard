package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.shared.primitive.NumberFormatter

class MeasurementValueMapper(
    private val formatNumber: NumberFormatter,
) {

    operator fun invoke(value: MeasurementValue): MeasurementValueForUser {
        val unit = value.property.selectedUnit
        return MeasurementValueForUser(
            value = formatNumber(value.value * unit.factor),
            unit = unit,
        )
    }

    operator fun invoke(value: MeasurementValueForDatabase): MeasurementValueForUser {
        val unit = value.unit
        return MeasurementValueForUser(
            value = formatNumber(value.value * unit.factor),
            unit = unit,
        )
    }

    operator fun invoke(value: MeasurementValueForUser): MeasurementValueForDatabase? {
        val number = value.value.toDoubleOrNull() ?: return null
        val unit = value.unit
        return MeasurementValueForDatabase(value = number / unit.factor, unit = unit)
    }
}