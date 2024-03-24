package com.faltenreich.diaguard.tag.form

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.tag.TagRepository

class CreateTagUseCase(
    private val repository: TagRepository,
    private val dateTimeFactory: DateTimeFactory,
) {

    operator fun invoke(name: String) {
        val now = dateTimeFactory.now()
        repository.create(
            createdAt = now,
            updatedAt = now,
            name = name,
        )
    }
}