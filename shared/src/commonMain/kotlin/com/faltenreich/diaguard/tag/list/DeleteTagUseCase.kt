package com.faltenreich.diaguard.tag.list

import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.TagRepository

class DeleteTagUseCase(
    private val repository: TagRepository,
) {

    operator fun invoke(tag: Tag) {
        repository.deleteById(tag.id)
    }
}