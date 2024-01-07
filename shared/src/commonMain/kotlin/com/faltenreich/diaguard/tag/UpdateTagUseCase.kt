package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.shared.di.inject

class UpdateTagUseCase(
    private val tagRepository: TagRepository = inject(),
) {

    operator fun invoke(tag: Tag) {
        tagRepository.update(tag)
    }
}