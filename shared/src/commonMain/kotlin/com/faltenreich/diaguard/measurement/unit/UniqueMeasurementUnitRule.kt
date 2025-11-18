package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.injection.inject
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.validation.ValidationRule
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.name_already_taken

class UniqueMeasurementUnitRule(
    private val repository: MeasurementUnitRepository = inject(),
    private val localization: Localization = inject(),
) : ValidationRule<MeasurementUnit> {

    override fun check(input: MeasurementUnit): ValidationResult<MeasurementUnit> {
        return when (repository.getByName(input.name)) {
            null -> ValidationResult.Success(input)
            input -> ValidationResult.Success(input)
            else -> ValidationResult.Failure(
                data = input,
                error = localization.getString(Res.string.name_already_taken),
            )
        }
    }
}