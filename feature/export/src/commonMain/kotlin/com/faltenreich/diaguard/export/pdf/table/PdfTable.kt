package com.faltenreich.diaguard.export.pdf.table

import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfTable(
    private val date: PdfDrawable,
    private val categories: PdfDrawable,
    private val notes: PdfDrawable,
) : PdfDrawable {

    private val dateSize = date.getSize()
    private val categoriesSize = categories.getSize()
    private val notesSize = notes.getSize()

    override fun getSize(): PdfSize {
        return PdfSize(
            width = maxOf(dateSize.width, categoriesSize.width, notesSize.width),
            height = dateSize.height + categoriesSize.height + notesSize.height,
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        date.drawOn(page, position)
        categories.drawOn(page, position.copy(y = position.y + dateSize.height))
        notes.drawOn(page, position.copy(y = position.y + dateSize.height + categoriesSize.height))
    }
}