package com.faltenreich.diaguard.tag

class UpdateTagUseCase(
    private val tagRepository: TagRepository,
) {

    operator fun invoke(tag: Tag) = with(tag) {
        tagRepository.update(
            id = id,
            name = name,
        )
    }
}