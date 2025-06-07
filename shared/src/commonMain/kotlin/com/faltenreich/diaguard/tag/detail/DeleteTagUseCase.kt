package com.faltenreich.diaguard.tag.detail

import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.TagRepository

class DeleteTagUseCase(private val repository: TagRepository) {

    operator fun invoke(tag: Tag.Local) {
        repository.delete(tag)
    }
}