package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.NumberFormatter

class MeasurementValueMapper(
    private val formatNumber: NumberFormatter,
    private val localization: Localization,
) {

    operator fun invoke(
        value: MeasurementValue,
        decimalPlaces: Int,
    ): MeasurementValue.Localized {
        val unit = value.property.selectedUnit
        return MeasurementValue.Localized(
            value = formatNumber(
                number = value.value * unit.factor,
                scale = decimalPlaces,
                locale = localization.getLocale(),
            ),
        )
    }

    operator fun invoke(
        value: Double,
        unit: MeasurementUnit,
        decimalPlaces: Int,
    ): MeasurementValue.Localized {
        return MeasurementValue.Localized(
            value = formatNumber(
                number = value * unit.factor,
                scale = decimalPlaces,
                locale = localization.getLocale(),
            ),
        )
    }

    operator fun invoke(
        value: String,
        unit: MeasurementUnit,
    ): Double? {
        val number = value.toDoubleOrNull() ?: return null
        return number / unit.factor
    }
}