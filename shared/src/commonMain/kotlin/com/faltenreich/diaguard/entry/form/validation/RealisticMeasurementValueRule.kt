package com.faltenreich.diaguard.entry.form.validation

import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInputState
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.validation.ValidationRule
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entry_form_error_unrealistic_value

class RealisticMeasurementValueRule(
    private val mapValue: MeasurementValueMapper = inject(),
    private val localization: Localization = inject(),
) : ValidationRule<MeasurementPropertyInputState> {

    override fun check(input: MeasurementPropertyInputState): ValidationResult<MeasurementPropertyInputState> {
        val unit = input.property.selectedUnit
        val value = mapValue(
            value = input.input,
            unit = unit,
        )
        val (minimumValue, maximumValue) = input.property.range.minimum to input.property.range.maximum
        return when (value) {
            null -> ValidationResult.Success(input)
            in minimumValue ..< maximumValue -> ValidationResult.Success(input)
            else -> ValidationResult.Failure(
                data = input,
                error = localization.getString(
                    Res.string.entry_form_error_unrealistic_value,
                    mapValue(
                        value = minimumValue,
                        unit = unit,
                        decimalPlaces = input.decimalPlaces,
                    ).value,
                    mapValue(
                        value = maximumValue,
                        unit = unit,
                        decimalPlaces = input.decimalPlaces,
                    ).value,
                ),
            )
        }
    }
}