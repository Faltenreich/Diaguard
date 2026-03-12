package com.faltenreich.diaguard.persistence.file

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.delete

class DeleteFileUseCase {

    suspend operator fun invoke(file: File) {
        PlatformFile(file.absolutePath).delete()
    }
}