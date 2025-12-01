package com.faltenreich.diaguard.entry.form.tag

import com.faltenreich.diaguard.data.tag.Tag

class GetTagSuggestionsUseCase {

    operator fun invoke(
        tags: Collection<Tag>,
        selection: Collection<Tag>,
        query: String,
    ): List<Tag> {
        return tags
            .filterNot { tag -> selection.any { it.name == tag.name } }
            .filter { tag ->
                if (query.isNotBlank()) tag.name.contains(query)
                else true
            }
            .take(LIMIT)
    }

    companion object Companion {

        private const val LIMIT = 5
    }
}