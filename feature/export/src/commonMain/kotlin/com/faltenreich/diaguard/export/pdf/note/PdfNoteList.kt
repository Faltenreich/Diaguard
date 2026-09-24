package com.faltenreich.diaguard.export.pdf.note

import com.faltenreich.diaguard.export.pdf.PdfBackground
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfNoteList(private val data: PdfNoteListData) : PdfDrawable {

    private val divider = PdfBackground(
        size = PdfSize(width = data.width, height = .75f),
        paint = PdfPaint.divider,
    )

    override fun getSize(): PdfSize {
        return PdfSize(
            width = data.width,
            height = data.rows.sumOf { it.content.getSize().height.toDouble() }.toFloat(),
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        if (data.rows.isNotEmpty()) {
            var position = position
            data.rows.forEach { row ->
                divider.drawOn(page, position)

                row.time.drawOn(page, position)
                row.content.drawOn(page, position.copy(x = position.x + data.timeWidth))
                position = position.copy(y = position.y + row.content.getSize().height)
            }
        }
    }
}