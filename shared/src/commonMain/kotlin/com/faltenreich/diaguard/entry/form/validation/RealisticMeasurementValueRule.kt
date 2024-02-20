package com.faltenreich.diaguard.entry.form.validation

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.form.measurement.MeasurementTypeInputState
import com.faltenreich.diaguard.measurement.value.InputValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueConverter
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.validation.ValidationRule

class RealisticMeasurementValueRule(
    private val measurementValueConverter: MeasurementValueConverter = inject(),
    private val numberFormatter: NumberFormatter = inject(),
    private val localization: Localization = inject(),
) : ValidationRule<MeasurementTypeInputState> {

    override fun check(input: MeasurementTypeInputState): ValidationResult<MeasurementTypeInputState> {
        val value = measurementValueConverter.convertToDefault(InputValue(input.input, input.type.selectedUnit))
        val (minimumValue, maximumValue) = input.type.minimumValue to input.type.maximumValue
        return when (value) {
            null -> ValidationResult.Success(input)
            in minimumValue ..< maximumValue -> ValidationResult.Success(input)
            else -> ValidationResult.Failure(
                input,
                error = localization.getString(
                    MR.strings.entry_form_error_unrealistic_value,
                    numberFormatter.invoke(minimumValue),
                    numberFormatter.invoke(maximumValue),
                ),
            )
        }
    }
}