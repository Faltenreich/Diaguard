package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.persistence.file.File

internal class PdfDocument {

    private val platformDocument = PdfPlatformDocument()
    lateinit var page: PdfPage

    fun open(file: File) {
        platformDocument.open(file)
    }

    fun close() {
        platformDocument.close()
    }

    fun addPage(page: PdfPage) {
        this.page = page
    }

    fun finishPage() {
        platformDocument.finishPage(page)
    }

    fun canMove(by: Float): Boolean {
        return page.canMove(by)
    }

    fun move(by: Float) {
        page.move(by)
    }
}