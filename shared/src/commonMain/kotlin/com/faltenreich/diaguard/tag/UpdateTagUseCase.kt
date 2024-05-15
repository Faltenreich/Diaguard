package com.faltenreich.diaguard.tag

class UpdateTagUseCase(
    private val tagRepository: TagRepository,
) {

    operator fun invoke(tag: Tag.Persistent) = with(tag) {
        tagRepository.update(
            id = id,
            name = name,
        )
    }
}