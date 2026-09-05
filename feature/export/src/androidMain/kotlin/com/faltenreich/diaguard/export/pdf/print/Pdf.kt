package com.faltenreich.diaguard.export.pdf.print

import android.graphics.pdf.PdfDocument
import android.util.Size
import androidx.compose.ui.geometry.Offset
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

internal class Pdf {

    private val document = PdfDocument()
    private lateinit var outputStream: OutputStream
    lateinit var page: PdfDocument.Page
    var offset: Offset = PAGE_PADDING
    val paint = PdfPaint.default

    fun open(file: File) {
        outputStream = FileOutputStream(file)
    }

    fun close() {
        document.close()
    }

    fun addPage() {
        val pageInfo = PdfDocument.PageInfo.Builder(
            DIN_A4.width,
            DIN_A4.height,
            document.pages.size,
        ).create()
        page = document.startPage(pageInfo)
        offset = PAGE_PADDING
    }

    fun closePage() {
        document.finishPage(page)
        document.writeTo(outputStream)
    }

    fun draw(drawable: PdfDrawable) {
        drawable.drawOn(this)
    }

    private companion object {

        private val DIN_A4 = Size(595, 842)
        private val PAGE_PADDING = Offset(60f, 60f)
    }
}