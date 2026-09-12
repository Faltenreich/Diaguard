package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.persistence.file.File

internal class PdfDocument(file: File) {

    val platformDocument = PdfPlatformDocument(file)

    val pageCount: Int get() = platformDocument.pageCount

    fun close() {
        platformDocument.write()
        platformDocument.close()
    }
}