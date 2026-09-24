package com.faltenreich.diaguard.export.pdf.note

import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal data class PdfNoteListData(
    val timeWidth: Float,
    val contentWidth: Float,
    val rows: List<Row>,
) {

    val size: PdfSize = PdfSize(
        width = timeWidth + contentWidth,
        height = rows.sumOf { it.content.getSize().height.toDouble() }.toFloat(),
    )

    data class Row(
        val time: PdfDrawable,
        val content: PdfDrawable,
    )
}