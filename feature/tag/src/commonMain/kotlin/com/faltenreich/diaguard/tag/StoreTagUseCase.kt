package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.data.tag.Tag
import com.faltenreich.diaguard.data.tag.TagRepository

class StoreTagUseCase(private val repository: TagRepository) {

    operator fun invoke(tag: Tag) {
        when (tag) {
            is Tag.Seed -> repository.create(tag)
            is Tag.Legacy -> repository.create(tag)
            is Tag.User -> repository.create(tag)
            is Tag.Local -> repository.update(tag)
        }
    }
}