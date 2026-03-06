package com.faltenreich.diaguard.persistence.file

class ShareFileUseCase(private val fileHandler: FileHandler) {

    operator fun invoke(file: File) {
        fileHandler.share(file)
    }
}