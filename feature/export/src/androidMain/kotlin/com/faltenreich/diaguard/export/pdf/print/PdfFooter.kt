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

        pageNumber?.let { pageNumber ->
            val position = PointF(
                page.viewport.width() - pageNumber.getSize(page).width,
                position.y,
            )
            pageNumber.drawOn(page, position)
        }
    }

    override fun getSize(page: PdfPage): Size {
        val dateOfExportSize = dateOfExport?.getSize(page) ?: Size(0, 0)
        val pageNumberSize = pageNumber?.getSize(page) ?: Size(0, 0)
        return Size(
            page.viewport.width().toInt(),
            max(dateOfExportSize.height, pageNumberSize.height),
        )
    }
}