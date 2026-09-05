package com.faltenreich.diaguard.export.pdf.print

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface

internal object PdfPaint {

    val normal: Paint = Paint().apply {
        color = Color.BLACK
        typeface = Typeface.DEFAULT
    }
    val bold: Paint = Paint().apply {
        color = Color.BLACK
        typeface = Typeface.DEFAULT_BOLD
    }
    val header: Paint = Paint().apply {
        color = Color.BLACK
        typeface = Typeface.DEFAULT_BOLD
        textSize = 14f
    }
}