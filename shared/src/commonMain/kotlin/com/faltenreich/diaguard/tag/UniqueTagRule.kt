package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.architecture.either.ValidationResult
import com.faltenreich.diaguard.architecture.either.ValidationRule
import com.faltenreich.diaguard.data.tag.Tag
import com.faltenreich.diaguard.data.tag.TagRepository
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