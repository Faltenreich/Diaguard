package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.persistence.file.File

internal expect class PdfPlatformDocument(file: File) {

    val pageCount: Int

    fun write()

    fun close()
}