package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.persistence.file.File

internal actual class PdfDocument actual constructor(file: File) {

    actual val pageCount: Int get() = TODO("Not yet implemented")

    actual fun close() {
        TODO("Not yet implemented")
    }
}