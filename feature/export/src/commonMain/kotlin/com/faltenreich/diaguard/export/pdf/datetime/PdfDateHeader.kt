package com.faltenreich.diaguard.export.pdf.datetime

import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfDateHeader(
    private val width: Float,
    private val date: PdfDrawable,
    private val hours: PdfDrawable?,
) : PdfDrawable {

    override fun getSize(): PdfSize {
        return PdfSize(
            width = width,
            height = maxOf(date.getSize().height, hours?.getSize()?.height ?: 0f),
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        date.drawOn(page, position)
        hours?.drawOn(page, position.copy(x = position.x + DATE_WIDTH))
    }

    private companion object {

        const val DATE_WIDTH = 100f
    }
}