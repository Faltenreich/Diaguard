package com.faltenreich.diaguard.tag.form

import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.TagRepository

class StoreTagUseCase(
    private val repository: TagRepository,
) {

    operator fun invoke(tag: Tag) {
        when (tag) {
            is Tag.User -> repository.create(tag)
            is Tag.Local -> repository.update(tag)
        }
    }
}