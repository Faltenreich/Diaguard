package com.faltenreich.diaguard.tag.form

import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.validation.ValidationRule
import com.faltenreich.diaguard.tag.Tag

class ValidateTagUseCase(private val rules: List<ValidationRule<Tag>>) {

    operator fun invoke(tag: Tag): ValidationResult<Tag> {
        return rules.map { it.check(tag) }
            .firstOrNull { it is ValidationResult.Failure }
            ?: ValidationResult.Success(tag)
    }
}