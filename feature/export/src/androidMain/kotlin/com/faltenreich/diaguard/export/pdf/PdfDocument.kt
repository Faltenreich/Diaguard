package com.faltenreich.diaguard.export.pdf

import android.graphics.pdf.PdfDocument
import com.faltenreich.diaguard.persistence.file.File
import java.io.FileOutputStream

internal actual class PdfDocument actual constructor(file: File) {

    val platform = PdfDocument()
    private val outputStream = FileOutputStream(java.io.File(file.absolutePath))

    actual val pageCount: Int get() = platform.pages.size

    actual fun close() {
        platform.writeTo(outputStream)
        platform.close()
    }
}