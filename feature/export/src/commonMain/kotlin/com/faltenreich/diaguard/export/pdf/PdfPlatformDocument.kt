package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.persistence.file.File

internal expect class PdfPlatformDocument() {

    fun open(file: File)

    fun close()

    fun finishPage(page: PdfPage)
}