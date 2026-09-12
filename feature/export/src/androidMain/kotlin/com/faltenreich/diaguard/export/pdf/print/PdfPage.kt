package com.faltenreich.diaguard.export.pdf.print

import android.graphics.PointF
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.util.Size

internal class PdfPage(
    private val header: PdfHeader?,
    private val footer: PdfFooter?,
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
            PAGE_PADDING + (header?.getSize()?.height ?: 0),
            pageWidth - PAGE_PADDING,
            pageHeight - PAGE_PADDING - (footer?.getSize()?.height ?: 0),
        )
        offset = PointF(viewport.left, viewport.top)

        header?.let(::draw)
        footer?.let(::draw)
    }

    fun finish(document: PdfDocument) {
        document.finishPage(page)
    }

    fun canMove(by: Int): Boolean {
        return offset.y + by <= viewport.bottom
    }

    fun move(by: Int) {
        offset.set(offset.x, offset.y + by.toFloat())
    }

    fun draw(drawable: PdfDrawable) {
        drawable.drawOn(page.canvas, offset)
    }

    private companion object {

        private val DIN_A4 = Size(595, 842)
        private const val PAGE_PADDING = 60f
    }
}