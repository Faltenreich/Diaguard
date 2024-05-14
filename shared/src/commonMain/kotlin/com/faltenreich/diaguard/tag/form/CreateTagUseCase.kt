package com.faltenreich.diaguard.tag.form

import com.faltenreich.diaguard.tag.TagRepository

class CreateTagUseCase(
    private val repository: TagRepository,
) {

    operator fun invoke(name: String) {
        repository.create(
            name = name,
        )
    }
}