package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.persistence.file.File

internal class PdfDocument(file: File) {

    val platformDocument = PdfPlatformDocument(file)
    lateinit var page: PdfPage

    val pageCount: Int get() = platformDocument.pageCount

    fun close() {
        platformDocument.close()
    }

    fun addPage(page: PdfPage) {
        this.page = page
    }

    fun finishPage() {
        page.finish()
        platformDocument.write()
    }

    fun canMove(by: Float): Boolean {
        return page.canMove(by)
    }

    fun move(by: Float) {
        page.move(by)
    }
}