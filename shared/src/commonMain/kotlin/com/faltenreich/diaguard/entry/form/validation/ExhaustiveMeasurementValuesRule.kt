package com.faltenreich.diaguard.entry.form.validation

import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInputState
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.validation.ValidationRule

class ExhaustiveMeasurementValuesRule : ValidationRule<MeasurementPropertyInputState> {

    override fun check(input: MeasurementPropertyInputState): ValidationResult<MeasurementPropertyInputState> {
        // TODO: Check whether one value for every type is given
        // TODO: Add this info to data class of MeasurementType (isMandatory?)
        return ValidationResult.Success(input)
    }
}