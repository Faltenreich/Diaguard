package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.shared.localization.NumberFormatter

class MapMeasurementValueRangeStateUseCase(
    private val numberFormatter: NumberFormatter,
) {

    operator fun invoke(
        property: MeasurementProperty,
        decimalPlaces: Int,
    ): MeasurementPropertyFormState.ValueRange {
        return MeasurementPropertyFormState.ValueRange(
            minimum = property.range.minimum.let { number ->
                numberFormatter(
                    number = number,
                    scale = decimalPlaces,
                )
            },
            low = property.range.low?.let { number ->
                numberFormatter(
                    number = number,
                    scale = decimalPlaces,
                )
            } ?: "",
            target = property.range.target?.let { number ->
                numberFormatter(
                    number = number,
                    scale = decimalPlaces,
                )
            } ?: "",
            high = property.range.high?.let { number ->
                numberFormatter(
                    number = number,
                    scale = decimalPlaces,
                )
            } ?: "",
            maximum = property.range.maximum.let { number ->
                numberFormatter(
                    number = number,
                    scale = decimalPlaces,
                )
            },
            isHighlighted = property.range.isHighlighted,
            unit = property.unit?.abbreviation,
        )
    }
}