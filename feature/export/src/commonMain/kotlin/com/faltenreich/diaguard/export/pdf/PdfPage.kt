package com.faltenreich.diaguard.export.pdf

import android.graphics.Canvas
import android.graphics.pdf.PdfDocument

internal class PdfPage(
    private val header: PdfHeader?,
    private val footer: PdfFooter?,
) {

    private lateinit var page: PdfDocument.Page

    lateinit var viewport: PdfRectangle
    lateinit var offset: PdfPosition
    val width: Float = viewport.width

    fun start(document: PdfDocument) {
        val pageWidth = DIN_A4.width
        val pageHeight = DIN_A4.height
        val pageInfo = PdfDocument.PageInfo.Builder(
            pageWidth,
            pageHeight,
            document.pages.size,
        ).create()
        page = document.startPage(pageInfo)

        viewport = PdfRectangle(
            PAGE_PADDING,
            PAGE_PADDING,
            pageWidth - PAGE_PADDING,
            pageHeight - PAGE_PADDING,
        )

        offset = PdfPosition(viewport.left, viewport.top)

        header?.let { header ->
            val height = header.getSize(this).height
            val position = PdfPosition(
                viewport.left,
                viewport.top,
            )
            header.drawOn(this, position)
            viewport = viewport.copy(top = viewport.top + height)
            move(height)
        }

        footer?.let { footer ->
            val height = footer.getSize(this).height
            val position = PdfPosition(
                viewport.left,
                viewport.bottom - height,
            )
            footer.drawOn(this, position)
            viewport = viewport.copy(bottom = viewport.bottom - height)
        }
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

    fun draw(block: Canvas.() -> Unit) {
        page.canvas.run(block)
    }

    private companion object {

        private val DIN_A4 = PdfSize(595f, 842f)
        private const val PAGE_PADDING = 60f
    }
}