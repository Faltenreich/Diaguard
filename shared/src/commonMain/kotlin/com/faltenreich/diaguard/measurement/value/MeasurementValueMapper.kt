package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestion
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.NumberFormatter

class MeasurementValueMapper(
    private val formatNumber: NumberFormatter,
    private val localization: Localization,
) {

    operator fun invoke(
        value: Double,
        unit: MeasurementUnit,
        decimalPlaces: Int,
        // TODO:
        suggestion: MeasurementUnitSuggestion? = null,
    ): MeasurementValue.Localized {
        return MeasurementValue.Localized(
            value = formatNumber(
                number = value * (suggestion?.let(MeasurementUnitSuggestion::factor) ?: 1.0),
                scale = decimalPlaces,
                locale = localization.getLocale(),
            ),
        )
    }

    operator fun invoke(
        value: MeasurementValue,
        decimalPlaces: Int,
    ): MeasurementValue.Localized {
        return invoke(
            value = value.value,
            unit = value.property.unit,
            // TODO:
            suggestion = null,
            decimalPlaces = decimalPlaces,
        )
    }

    operator fun invoke(
        value: String,
        unit: MeasurementUnit,
        // TODO:
        suggestion: MeasurementUnitSuggestion? = null,
    ): Double? {
        val number = value.toDoubleOrNull() ?: return null
        return number / (suggestion?.let(MeasurementUnitSuggestion::factor) ?: 1.0)
    }
}