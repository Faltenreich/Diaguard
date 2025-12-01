package com.faltenreich.diaguard.entry.form.validation

import com.faltenreich.diaguard.architecture.either.ValidationResult
import com.faltenreich.diaguard.architecture.either.ValidationRule
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInputState
import com.faltenreich.diaguard.injection.inject
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.entry_form_error_unrealistic_value

class RealisticMeasurementValueRule(
    private val mapValue: MeasurementValueMapper = inject(),
    private val localization: Localization = inject(),
) : ValidationRule<MeasurementPropertyInputState> {

    override fun check(input: MeasurementPropertyInputState): ValidationResult<MeasurementPropertyInputState> {
        val property = input.property
        val value = mapValue(input.input, property)
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
                        property = property,
                        decimalPlaces = input.decimalPlaces,
                    ).value,
                    mapValue(
                        value = maximumValue,
                        property = property,
                        decimalPlaces = input.decimalPlaces,
                    ).value,
                ),
            )
        }
    }
}