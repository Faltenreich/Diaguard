package com.faltenreich.diaguard.entry.form.tag

import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.TagRepository
import kotlinx.coroutines.flow.Flow

class GetTagsByQueryUseCase(
    private val repository: TagRepository,
) {

    operator fun invoke(query: String): Flow<List<Tag>> {
        // TODO: Limit results?
        return query.takeIf(String::isNotBlank)?.let(repository::observeByQuery) ?: repository.observeAll()
    }
}