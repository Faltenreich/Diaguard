package com.faltenreich.diaguard.tag.detail

import com.faltenreich.diaguard.data.tag.Tag
import com.faltenreich.diaguard.data.tag.TagRepository

class DeleteTagUseCase(private val repository: TagRepository) {

    operator fun invoke(tag: Tag.Local) {
        repository.delete(tag)
    }
}