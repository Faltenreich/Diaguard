package com.faltenreich.diaguard.export.pdf.datetime

import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfDateWithHours(
    private val date: PdfDrawable,
    private val hours: PdfDrawable,
) : PdfDrawable {

    private val dateSize = date.getSize()
    private val hoursSize = hours.getSize()

    override fun getSize(): PdfSize {
        return PdfSize(
            width = dateSize.width + hoursSize.width,
            height = maxOf(dateSize.height, hoursSize.height),
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        date.drawOn(page, position)
        hours.drawOn(page, position.copy(x = position.x + dateSize.width))
    }
}