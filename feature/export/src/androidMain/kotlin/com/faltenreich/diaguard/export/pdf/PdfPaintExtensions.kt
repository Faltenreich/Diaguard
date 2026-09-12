package com.faltenreich.diaguard.export.pdf

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.graphics.toArgb

internal fun PdfPaint.toPlatform(): Paint {
    val paint = this
    return Paint().apply {
        color = paint.color.toArgb()
        typeface = when (paint.typeface) {
            PdfTypeface.NORMAL -> Typeface.DEFAULT
            PdfTypeface.BOLD,
            PdfTypeface.HEADER -> Typeface.DEFAULT_BOLD
        }
        textSize = paint.textSize
    }
}