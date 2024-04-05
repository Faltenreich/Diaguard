package com.faltenreich.diaguard.tag.form

import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.validation.ValidationRule
import com.faltenreich.diaguard.tag.TagRepository

class UniqueTagRule(
    private val repository: TagRepository = inject(),
    private val localization: Localization = inject(),
) : ValidationRule<String> {

    override fun check(input: String): ValidationResult<String> {
        return when (repository.getByName(input)) {
            null -> ValidationResult.Success(input)
            else -> ValidationResult.Failure(input, error = localization.getString(Res.string.tag_already_taken))
        }
    }
}