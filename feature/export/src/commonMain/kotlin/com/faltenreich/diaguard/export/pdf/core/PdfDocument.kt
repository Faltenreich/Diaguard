package com.faltenreich.diaguard.export.pdf.core

import com.faltenreich.diaguard.persistence.file.File

internal expect class PdfDocument(file: File) {

    fun countPages(): Int

    fun close()
}