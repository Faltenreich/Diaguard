package com.faltenreich.diaguard.export.pdf

import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize
import kotlin.math.max

internal class PdfFooter(
    dateOfExport: String?,
    pageNumber: String?,
) : PdfDrawable {

    private val dateOfExport: PdfText? = dateOfExport?.let { PdfText(it, PdfPaint.normal) }
    private val pageNumber: PdfText? = pageNumber?.let { PdfText(it, PdfPaint.normal) }

    override fun getSize(): PdfSize {
        val dateOfExportSize = dateOfExport?.getSize() ?: PdfSize.Zero
        val pageNumberSize = pageNumber?.getSize() ?: PdfSize.Zero
        return PdfSize(
            width = -1f, // Unknown yet
            height = max(dateOfExportSize.height, pageNumberSize.height),
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        dateOfExport?.drawOn(page, position)

        pageNumber?.let { pageNumber ->
            val position = PdfPosition(
                x = page.viewport.right - pageNumber.getSize().width,
                y = position.y,
            )
            pageNumber.drawOn(page, position)
        }
    }
}