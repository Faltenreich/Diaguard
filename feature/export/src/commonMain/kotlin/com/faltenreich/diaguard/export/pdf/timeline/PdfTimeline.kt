package com.faltenreich.diaguard.export.pdf.timeline

import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfTimeline(
    private val date: PdfDrawable,
    private val chart: PdfDrawable,
    private val table: PdfDrawable,
    private val notes: PdfDrawable,
) : PdfDrawable {

    private val dateSize = date.getSize()
    private val chartSize = chart.getSize()
    private val tableSize = table.getSize()
    private val notesSize = notes.getSize()

    override fun getSize(): PdfSize {
        return PdfSize(
            width = maxOf(dateSize.width, chartSize.width, tableSize.width, notesSize.width),
            height = dateSize.height + chartSize.height + tableSize.height + notesSize.height,
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        var y = position.y

        date.drawOn(page, position)
        y += dateSize.height

        chart.drawOn(page, position.copy(y = y))
        y += chartSize.height

        table.drawOn(page, position.copy(y = y))
        y += tableSize.height

        notes.drawOn(page, position.copy(y = y))
    }
}