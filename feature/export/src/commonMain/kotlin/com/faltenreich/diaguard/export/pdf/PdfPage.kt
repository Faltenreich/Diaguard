package com.faltenreich.diaguard.export.pdf

internal class PdfPage(
    document: PdfDocument,
    header: PdfCalendarWeek?,
    footer: PdfFooter?,
) {

    private var platformPage: PdfPlatformPage

    var viewport: PdfRectangle
    var offset: PdfPosition

    init {
        val size = DIN_A4
        platformPage = PdfPlatformPage(document, size)

        viewport = PdfRectangle(
            PAGE_PADDING,
            PAGE_PADDING,
            size.width - PAGE_PADDING,
            size.height - PAGE_PADDING,
        )

        offset = PdfPosition(x = viewport.left, y = viewport.top)

        header?.let { header ->
            val height = header.getSize(this).height
            val position = PdfPosition(x = viewport.left, y = viewport.top)
            header.drawOn(this, position)
            viewport = viewport.copy(top = viewport.top + height)
            move(height)
        }

        footer?.let { footer ->
            val height = footer.getSize(this).height
            val position = PdfPosition(x = viewport.left, y = viewport.bottom - height)
            footer.drawOn(this, position)
            viewport = viewport.copy(bottom = viewport.bottom - height)
        }
    }

    fun finish() {
        platformPage.finish()
    }

    fun canMove(by: Float): Boolean {
        return offset.y + by <= viewport.bottom
    }

    fun move(by: Float) {
        offset = offset.copy(x = offset.x, y = offset.y + by)
    }

    fun drawText(text: String, position: PdfPosition, paint: PdfPaint) {
        platformPage.drawText(text, position, paint)
    }

    fun drawRectangle(rectangle: PdfRectangle, paint: PdfPaint) {
        platformPage.drawRectangle(rectangle, paint)
    }

    fun getTextBounds(text: String, paint: PdfPaint): PdfSize {
        return platformPage.getTextBounds(text, paint)
    }

    private companion object {

        private val DIN_A4 = PdfSize(595f, 842f)
        private const val PAGE_PADDING = 60f
    }
}