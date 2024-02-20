package com.faltenreich.diaguard.entry.form.validation

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.form.measurement.MeasurementTypeInputState
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
) : ValidationRule<MeasurementTypeInputState> {

    override fun check(input: MeasurementTypeInputState): ValidationResult<MeasurementTypeInputState> {
        val valueForUser = MeasurementValueForUser(input.input, input.type.selectedUnit)
        val valueForDatabase = mapValue(valueForUser)
        val (minimumValue, maximumValue) = input.type.minimumValue to input.type.maximumValue
        return when (valueForDatabase?.value) {
            null -> ValidationResult.Success(input)
            in minimumValue ..< maximumValue -> ValidationResult.Success(input)
            else -> ValidationResult.Failure(
                input,
                error = localization.getString(
                    MR.strings.entry_form_error_unrealistic_value,
                    formatNumber(minimumValue),
                    formatNumber(maximumValue),
                ),
            )
        }
    }
}