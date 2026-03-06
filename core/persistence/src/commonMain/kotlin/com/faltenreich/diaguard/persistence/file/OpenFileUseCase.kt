package com.faltenreich.diaguard.persistence.file

class OpenFileUseCase(private val fileOpener: FileOpener) {

    operator fun invoke(file: File) {
        fileOpener.open(file)
    }
}