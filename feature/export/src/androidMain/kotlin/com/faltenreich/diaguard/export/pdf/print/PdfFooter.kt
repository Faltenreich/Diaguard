package com.faltenreich.diaguard.export.pdf.print

import android.graphics.PointF
import android.util.Size
import kotlin.math.max

internal class PdfFooter(
    private val dateOfExport: PdfText?,
    private val pageNumber: PdfText?,
) : PdfDrawable {

    override fun drawOn(page: PdfPage, position: PointF) {
        dateOfExport?.drawOn(page, position)
        pageNumber?.let {
            // TODO: Get parent width
            val x = 500f - pageNumber.getSize(page).width
            pageNumber.drawOn(page, PointF(x, position.y))
        }
    }

    override fun getSize(page: PdfPage): Size {
        val dateOfExportSize = dateOfExport?.getSize(page) ?: Size(0, 0)
        val pageNumberSize = pageNumber?.getSize(page) ?: Size(0, 0)
        return Size(
            page.canvas.width,
            max(dateOfExportSize.height, pageNumberSize.height),
        )
    }
}