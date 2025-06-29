package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.validation.ValidationRule
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.name_already_taken

class UniqueTagRule(
    private val repository: TagRepository,
    private val localization: Localization,
) : ValidationRule<Tag> {

    override fun check(input: Tag): ValidationResult<Tag> {
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