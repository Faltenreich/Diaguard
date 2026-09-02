package com.faltenreich.diaguard.export.pdf.print

import android.graphics.pdf.PdfDocument
import androidx.compose.ui.geometry.Offset
import android.graphics.Paint as PdfPaint

internal data class PdfState(
    val page: PdfDocument.Page,
    val offset: Offset,
    val paint: PdfState.Paint,
) {

    data class Paint(
        val normal: PdfPaint,
        val bold: PdfPaint,
        val header: PdfPaint,
    )
}