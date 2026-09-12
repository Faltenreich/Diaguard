package com.faltenreich.diaguard.export.pdf

import android.graphics.pdf.PdfDocument
import com.faltenreich.diaguard.persistence.file.File
import java.io.FileOutputStream

internal actual class PdfDocument actual constructor(file: File) {

    private val document = PdfDocument()
    private val outputStream = FileOutputStream(java.io.File(file.absolutePath))

    actual fun countPages(): Int {
        return document.pages.size
    }

    actual fun close() {
        document.writeTo(outputStream)
        document.close()
    }

    fun <T> runNative(block: PdfDocument.() -> T): T {
        return document.run(block)
    }
}