package com.faltenreich.diaguard.export.pdf

import kotlin.math.max

internal class PdfFooter(
    dateOfExport: String?,
    pageNumber: String?,
) : PdfDrawable {

    private val dateOfExport: PdfText? = dateOfExport?.let { PdfText(it, PdfPaint.normal) }
    private val pageNumber: PdfText? = pageNumber?.let { PdfText(it, PdfPaint.normal) }

    override fun getSize(page: PdfPage): PdfSize {
        val dateOfExportSize = dateOfExport?.getSize(page) ?: PdfSize.Zero
        val pageNumberSize = pageNumber?.getSize(page) ?: PdfSize.Zero
        return PdfSize(
            width = page.viewport.width,
            height = max(dateOfExportSize.height, pageNumberSize.height),
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        dateOfExport?.drawOn(page, position)

        pageNumber?.let { pageNumber ->
            val position = PdfPosition(
                x = page.viewport.right - pageNumber.getSize(page).width,
                y = position.y,
            )
            pageNumber.drawOn(page, position)
        }
    }
}