package com.faltenreich.diaguard.tag

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