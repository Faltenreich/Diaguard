package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.persistence.file.File

internal expect class PdfPlatformDocument(file: File) {

    fun write()

    fun close()
}