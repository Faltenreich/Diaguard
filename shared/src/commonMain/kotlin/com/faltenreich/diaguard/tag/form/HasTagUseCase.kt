package com.faltenreich.diaguard.tag.form

import com.faltenreich.diaguard.tag.TagRepository

class HasTagUseCase(
    private val repository: TagRepository,
) {

    operator fun invoke(name: String): Boolean {
        return repository.getByName(name) != null
    }
}