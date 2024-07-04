package com.faltenreich.diaguard.preference.decimal

import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.validation.ValidationRule

class ValidateDecimalPlacesUseCase(
    private val rules: List<ValidationRule<Int>>,
) {

    operator fun invoke(decimalPlaces: Int): ValidationResult<Int> {
        return rules
            .map { it.check(decimalPlaces) }
            .firstOrNull { it is ValidationResult.Failure }
            ?: ValidationResult.Success(decimalPlaces)
    }
}