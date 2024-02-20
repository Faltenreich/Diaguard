package com.faltenreich.diaguard.tag.form

import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.validation.ValidationRule

class ValidateTagUseCase(private val rules: List<ValidationRule<String>>) {

    operator fun invoke(input: String): ValidationResult<String> {
        return rules.map { it.check(input) }
            .firstOrNull { it is ValidationResult.Failure }
            ?: ValidationResult.Success(input)
    }
}