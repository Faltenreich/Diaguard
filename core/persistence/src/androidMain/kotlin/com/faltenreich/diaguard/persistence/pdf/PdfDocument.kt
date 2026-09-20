package com.faltenreich.diaguard.persistence.pdf

import android.graphics.pdf.PdfDocument
import com.faltenreich.diaguard.persistence.file.File
import java.io.FileOutputStream

actual class PdfDocument actual constructor(file: File) {

    private val actual = PdfDocument()
    private val outputStream = FileOutputStream(java.io.File(file.absolutePath))

    actual fun countPages(): Int {
        return actual.pages.size
    }

    actual fun close() {
        actual.writeTo(outputStream)
        actual.close()
    }

    fun <T> runNative(block: PdfDocument.() -> T): T {
        return actual.run(block)
    }
}