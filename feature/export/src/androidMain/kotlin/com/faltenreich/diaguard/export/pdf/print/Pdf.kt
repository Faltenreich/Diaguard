package com.faltenreich.diaguard.export.pdf.print

import android.graphics.pdf.PdfDocument
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

internal class Pdf {

    private val document = PdfDocument()
    private lateinit var outputStream: OutputStream
    private lateinit var page: PdfPage

    fun open(file: File) {
        outputStream = FileOutputStream(file)
    }

    fun close() {
        document.close()
    }

    fun addPage(page: PdfPage) {
        this.page = page.apply { start(document) }
    }

    fun finishPage() {
        page.finish(document)
        document.writeTo(outputStream)
    }

    fun draw(drawable: PdfDrawable) {
        page.draw(drawable)

        val height = drawable.getSize().height
        if (page.canMove(height)) {
            page.move(height)
        } else {
            finishPage()
            addPage(PdfPage(page.header, page.footer))
        }
    }
}