package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.persistence.file.File

internal expect class PdfDocument(file: File) {

    val pageCount: Int

    fun close()
}