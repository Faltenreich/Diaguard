package com.faltenreich.diaguard.export

import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import java.io.File
import java.io.FileOutputStream

actual class PdfExport {

    actual fun export() {

    }

    fun export(file: File) {
        val outputStream = FileOutputStream(file)
        val document = PdfDocument()

        val pageNumber = 1
        val pageInfo = PageInfo.Builder(PDF_PAGE_WIDTH, PDF_PAGE_HEIGHT, pageNumber).create()
        val page = document.startPage(pageInfo)

        page.canvas.drawText("Hello, World!", 0f, 0f, Paint().apply { color = Color.BLACK })

        document.finishPage(page)
        document.writeTo(outputStream)
        document.close()
    }

    companion object {

        // DIN A4
        private const val PDF_PAGE_WIDTH = 595
        private const val PDF_PAGE_HEIGHT = 842
    }
}