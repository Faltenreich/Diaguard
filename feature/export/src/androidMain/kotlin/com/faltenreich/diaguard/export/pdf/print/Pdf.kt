package com.faltenreich.diaguard.export.pdf.print

import android.graphics.pdf.PdfDocument
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

internal class Pdf {

    private val document = PdfDocument()
    private lateinit var outputStream: OutputStream
    lateinit var page: PdfPage

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

    fun canMove(by: Int): Boolean {
        return page.canMove(by)
    }

    fun move(by: Int) {
        page.move(by)
    }

    fun draw(drawable: PdfDrawable) {
        page.draw(drawable)
    }
}