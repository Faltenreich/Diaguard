package com.faltenreich.diaguard.entry.form.tag

import com.faltenreich.diaguard.data.tag.Tag
import com.faltenreich.diaguard.data.tag.TagRepository
import kotlinx.coroutines.flow.Flow

class GetTagsUseCase(
    private val repository: TagRepository,
) {

    operator fun invoke(): Flow<List<Tag.Local>> {
        return repository.observeAll()
    }
}