package com.faltenreich.diaguard.persistence.pdf

import com.faltenreich.diaguard.persistence.file.File

expect class PdfDocument(file: File) {

    fun countPages(): Int

    fun close()
}