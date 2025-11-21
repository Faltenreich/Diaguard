package com.faltenreich.diaguard.tag.detail

import com.faltenreich.diaguard.data.tag.Tag
import com.faltenreich.diaguard.data.tag.TagRepository

class GetTagByIdUseCase(
    private val repository: TagRepository,
) {

    operator fun invoke(id: Long): Tag.Local? {
        return repository.getById(id)
    }
}