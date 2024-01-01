package com.faltenreich.diaguard.tag.form

import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.validation.Rule
import com.faltenreich.diaguard.tag.TagRepository

class UniqueTagRule(
    private val repository: TagRepository = inject(),
) : Rule<String> {

    override fun check(input: String): Result<Unit> {
        return when (repository.getByName(input)) {
            null -> Result.success(Unit)
            else -> Result.failure(RedundantTagException(input))
        }
    }
}