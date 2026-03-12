package com.faltenreich.diaguard.persistence.file

class OpenFileUseCase(private val fileHandler: FileHandler) {

    operator fun invoke(file: File) {
        fileHandler.open(file)
    }
}