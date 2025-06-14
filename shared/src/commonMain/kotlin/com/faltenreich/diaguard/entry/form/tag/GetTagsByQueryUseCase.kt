package com.faltenreich.diaguard.entry.form.tag

import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.TagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTagsByQueryUseCase(
    private val repository: TagRepository,
) {

    operator fun invoke(
        query: String,
        other: List<Tag>,
    ): Flow<List<Tag>> {
        val tags = query.takeIf(String::isNotBlank)?.let(repository::observeByQuery)
            ?: repository.observeAll()
        return tags.excluding(other).limited()
    }

    private fun Flow<List<Tag>>.excluding(other: List<Tag>): Flow<List<Tag>> {
        return map { tags -> tags.filterNot { tag -> tag in other } }
    }

    private fun Flow<List<Tag>>.limited(): Flow<List<Tag>> {
        return map { tags -> tags.take(LIMIT) }
    }

    companion object {

        private const val LIMIT = 3
    }
}