package com.faltenreich.diaguard.measurement.unit.usecase

import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.architecture.either.ValidationResult
import com.faltenreich.diaguard.architecture.either.ValidationRule

class ValidateMeasurementUnitUseCase(private val rules: List<ValidationRule<MeasurementUnit>>) {

    operator fun invoke(unit: MeasurementUnit): ValidationResult<MeasurementUnit> {
        return rules.map { it.check(unit) }
            .firstOrNull { it is ValidationResult.Failure }
            ?: ValidationResult.Success(unit)
    }
}