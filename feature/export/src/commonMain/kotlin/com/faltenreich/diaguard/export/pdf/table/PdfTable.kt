package com.faltenreich.diaguard.export.pdf.table

import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfTable(
    private val width: Float,
    private val date: PdfDrawable,
    private val categories: PdfDrawable,
    private val notes: PdfDrawable,
) : PdfDrawable {

    override fun getSize(): PdfSize {
        return PdfSize(
            width = width,
            height = date.getSize().height + categories.getSize().height + notes.getSize().height,
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        date.drawOn(page, position)
        categories.drawOn(page, position.copy(y = position.y + date.getSize().height))
        notes.drawOn(
            page,
            position.copy(y = position.y + getSize().height - notes.getSize().height)
        )
    }
}