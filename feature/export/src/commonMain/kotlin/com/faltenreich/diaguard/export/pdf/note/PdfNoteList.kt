package com.faltenreich.diaguard.export.pdf.note

import com.faltenreich.diaguard.export.pdf.PdfBackground
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfNoteList(
    private val timeWidth: Float,
    private val contentWidth: Float,
    private val rows: List<Row>,
) : PdfDrawable {

    private val width = timeWidth + contentWidth
    private val divider = PdfBackground(
        size = PdfSize(width = width, height = .75f),
        paint = PdfPaint.divider,
    )

    data class Row(
        val time: PdfDrawable,
        val content: PdfDrawable,
    )

    override fun getSize(): PdfSize {
        return PdfSize(
            width = width,
            height = rows.sumOf { it.content.getSize().height.toDouble() }.toFloat(),
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        if (rows.isNotEmpty()) {
            var position = position
            rows.forEach { row ->
                divider.drawOn(page, position)

                row.time.drawOn(page, position)
                row.content.drawOn(page, position.copy(x = position.x + timeWidth))
                position = position.copy(y = position.y + row.content.getSize().height)
            }
        }
    }
}