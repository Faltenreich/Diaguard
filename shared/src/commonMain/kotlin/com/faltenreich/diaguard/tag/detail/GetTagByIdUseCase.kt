package com.faltenreich.diaguard.tag.detail

import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.TagRepository

class GetTagByIdUseCase(
    private val repository: TagRepository,
) {

    operator fun invoke(id: Long): Tag.Local? {
        return repository.getById(id)
    }
}