package com.faltenreich.diaguard.entry.form.validation

import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInputState
import com.faltenreich.diaguard.measurement.value.MeasurementValueForUser
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.validation.ValidationRule

class RealisticMeasurementValueRule(
    private val mapValue: MeasurementValueMapper = inject(),
    private val formatNumber: NumberFormatter = inject(),
    private val localization: Localization = inject(),
) : ValidationRule<MeasurementPropertyInputState> {

    override fun check(input: MeasurementPropertyInputState): ValidationResult<MeasurementPropertyInputState> {
        val valueForUser = MeasurementValueForUser(input.input, input.property.selectedUnit)
        val valueForDatabase = mapValue(valueForUser)
        val (minimumValue, maximumValue) = input.property.range.minimum to input.property.range.maximum
        return when (valueForDatabase?.value) {
            null -> ValidationResult.Success(input)
            in minimumValue ..< maximumValue -> ValidationResult.Success(input)
            else -> ValidationResult.Failure(
                input,
                error = localization.getString(
                    Res.string.entry_form_error_unrealistic_value,
                    formatNumber(minimumValue),
                    formatNumber(maximumValue),
                ),
            )
        }
    }
}