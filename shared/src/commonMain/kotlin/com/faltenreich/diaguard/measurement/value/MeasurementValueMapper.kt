package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.localization.NumberFormatter

class MeasurementValueMapper(
    private val formatNumber: NumberFormatter,
) {

    operator fun invoke(
        value: Double,
        property: MeasurementProperty.Local,
        decimalPlaces: Int,
    ): MeasurementValue.Localized {
        return MeasurementValue.Localized(
            value = formatNumber(
                number = value * property.valueFactor,
                scale = decimalPlaces,
            ),
        )
    }

    operator fun invoke(
        value: MeasurementValue,
        decimalPlaces: Int,
    ): MeasurementValue.Localized {
        return invoke(
            value = value.value,
            property = value.property,
            decimalPlaces = decimalPlaces,
        )
    }

    operator fun invoke(
        value: String,
        property: MeasurementProperty.Local,
    ): Double? {
        val number = value.toDoubleOrNull() ?: return null
        return number / property.valueFactor
    }
}