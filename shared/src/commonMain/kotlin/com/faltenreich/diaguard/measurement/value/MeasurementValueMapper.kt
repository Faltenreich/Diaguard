package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.NumberFormatter

class MeasurementValueMapper(
    private val formatNumber: NumberFormatter,
    private val localization: Localization,
) {

    operator fun invoke(
        value: MeasurementValue,
        decimalPlaces: Int,
    ): MeasurementValueForUser {
        val unit = value.property.selectedUnit
        return MeasurementValueForUser(
            value = formatNumber(
                number = value.value * unit.factor,
                scale = decimalPlaces,
                locale = localization.getLocale(),
            ),
            unit = unit,
        )
    }

    operator fun invoke(
        value: MeasurementValueForDatabase,
        decimalPlaces: Int,
    ): MeasurementValueForUser {
        val unit = value.unit
        return MeasurementValueForUser(
            value = formatNumber(
                number = value.value * unit.factor,
                scale = decimalPlaces,
                locale = localization.getLocale(),
            ),
            unit = unit,
        )
    }

    operator fun invoke(value: MeasurementValueForUser): MeasurementValueForDatabase? {
        val number = value.value.toDoubleOrNull() ?: return null
        val unit = value.unit
        return MeasurementValueForDatabase(value = number / unit.factor, unit = unit)
    }
}