package com.faltenreich.diaguard.export.pdf.note

import com.faltenreich.diaguard.persistence.pdf.PdfDrawable

internal data class PdfNoteListData(
    val width: Float,
    val rows: List<Row>,
) {

    val timeWidth: Float = TIME_WIDTH
    val contentWidth: Float = width - timeWidth

    data class Row(
        val time: PdfDrawable,
        val content: PdfDrawable,
    )

    private companion object {

        const val TIME_WIDTH = 100f
    }
}