package com.faltenreich.diaguard.tag.list

import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.TagRepository
import kotlinx.coroutines.flow.Flow

class GetTagsUseCase(
    private val repository: TagRepository,
) {

    operator fun invoke(): Flow<List<Tag>> {
        return repository.observeAll()
    }
}