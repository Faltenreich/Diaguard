package com.faltenreich.diaguard.export.pdf.log

import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfLog(
    private val date: PdfDrawable,
    private val entries: PdfLogEntries,
) : PdfDrawable {

    private val dateSize = date.getSize()
    private val entriesSize = entries.getSize()

    override fun getSize(): PdfSize {
        return PdfSize(
            width = dateSize.width,
            height = dateSize.height + entriesSize.height,
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        date.drawOn(page, position)
        entries.drawOn(page, position.copy(y = position.y + dateSize.height))
    }
}