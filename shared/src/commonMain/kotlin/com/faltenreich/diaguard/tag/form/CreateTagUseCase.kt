package com.faltenreich.diaguard.tag.form

import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.TagRepository

class CreateTagUseCase(
    private val repository: TagRepository,
) {

    operator fun invoke(tag: Tag.Transfer) {
        repository.create(tag)
    }
}