package com.faltenreich.diaguard.export.pdf.note

import com.faltenreich.diaguard.persistence.pdf.PdfDrawable

internal data class PdfNoteListData(
    val timeWidth: Float,
    val contentWidth: Float,
    val rows: List<Row>,
) {

    val width: Float = timeWidth + contentWidth

    data class Row(
        val time: PdfDrawable,
        val content: PdfDrawable,
    )
}