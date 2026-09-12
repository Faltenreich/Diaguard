package com.faltenreich.diaguard.export.pdf

import android.graphics.pdf.PdfDocument
import com.faltenreich.diaguard.persistence.file.File
import java.io.FileOutputStream

internal actual class PdfPlatformDocument actual constructor(file: File) {

    val document = PdfDocument()
    private val outputStream = FileOutputStream(java.io.File(file.absolutePath))

    actual fun close() {
        document.close()
    }

    actual fun write() {
        document.writeTo(outputStream)
    }
}