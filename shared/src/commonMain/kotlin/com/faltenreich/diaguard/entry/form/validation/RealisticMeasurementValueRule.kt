package com.faltenreich.diaguard.entry.form.validation

import com.faltenreich.diaguard.entry.form.measurement.MeasurementTypeInputState
import com.faltenreich.diaguard.measurement.value.MeasurementValueFormatter
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.validation.ValidationRule

class RealisticMeasurementValueRule(
    private val measurementValueFormatter: MeasurementValueFormatter = inject(),
) : ValidationRule<MeasurementTypeInputState> {

    override fun check(input: MeasurementTypeInputState): ValidationResult<MeasurementTypeInputState> {
        // TODO: Check whether values are within MeasurementType.minimumValue and .maximumValue
        return ValidationResult.Success(input)
    }
}