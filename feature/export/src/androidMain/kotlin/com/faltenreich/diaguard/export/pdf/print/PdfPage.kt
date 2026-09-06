package com.faltenreich.diaguard.export.pdf.print

import android.graphics.PointF
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.util.Size

internal class PdfPage(
    val header: PdfHeader?,
    val footer: PdfFooter?,
) {

    private lateinit var page: PdfDocument.Page

    private lateinit var viewport: RectF
    private lateinit var offset: PointF

    fun start(document: PdfDocument) {
        val pageWidth = DIN_A4.width
        val pageHeight = DIN_A4.height
        val pageInfo = PdfDocument.PageInfo.Builder(
            pageWidth,
            pageHeight,
            document.pages.size,
        ).create()
        page = document.startPage(pageInfo)
        viewport = RectF(
            PAGE_PADDING,
            PAGE_PADDING,
            pageWidth - PAGE_PADDING,
            pageHeight - PAGE_PADDING,
        )
        offset = PointF(viewport.left, viewport.top)
    }

    fun finish(document: PdfDocument) {
        document.finishPage(page)
    }

    fun draw(drawable: PdfDrawable) {
        drawable.drawOn(page.canvas, offset)
    }

    fun canMove(by: Int): Boolean {
        return offset.y + by <= viewport.bottom
    }

    fun move(by: Int) {
        offset.set(offset.x, offset.y + by.toFloat())
    }

    private companion object {

        private val DIN_A4 = Size(595, 842)
        private const val PAGE_PADDING = 60f
    }
}