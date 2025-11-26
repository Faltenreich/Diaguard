package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.architecture.either.ValidationResult
import com.faltenreich.diaguard.architecture.either.ValidationRule
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.injection.inject
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.name_already_taken

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