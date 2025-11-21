package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.architecture.either.ValidationResult
import com.faltenreich.diaguard.architecture.either.ValidationRule

class ValidateTagUseCase(private val rules: List<ValidationRule<Tag>>) {

    operator fun invoke(tag: Tag): ValidationResult<Tag> {
        return rules.map { it.check(tag) }
            .firstOrNull { it is ValidationResult.Failure }
            ?: ValidationResult.Success(tag)
    }
}