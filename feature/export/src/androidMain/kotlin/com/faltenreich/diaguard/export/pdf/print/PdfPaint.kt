package com.faltenreich.diaguard.export.pdf.print

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface

data class PdfPaint(
    val normal: Paint,
    val bold: Paint,
    val header: Paint,
) {

    companion object {

        val default = PdfPaint(
            normal = Paint().apply {
                color = Color.BLACK
                typeface = Typeface.DEFAULT
            },
            bold = Paint().apply {
                color = Color.BLACK
                typeface = Typeface.DEFAULT_BOLD
            },
            header = Paint().apply {
                color = Color.BLACK
                typeface = Typeface.DEFAULT_BOLD
                textSize = 14f
            },
        )
    }
}