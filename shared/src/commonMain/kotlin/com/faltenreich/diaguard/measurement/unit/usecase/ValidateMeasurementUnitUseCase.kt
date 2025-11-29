package com.faltenreich.diaguard.measurement.unit.usecase

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.validation.ValidationRule

class ValidateMeasurementUnitUseCase(private val rules: List<ValidationRule<MeasurementUnit>>) {

    operator fun invoke(unit: MeasurementUnit): ValidationResult<MeasurementUnit> {
        return rules.map { it.check(unit) }
            .firstOrNull { it is ValidationResult.Failure }
            ?: ValidationResult.Success(unit)
    }
}