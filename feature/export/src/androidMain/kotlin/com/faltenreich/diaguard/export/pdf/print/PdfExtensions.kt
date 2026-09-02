package com.faltenreich.diaguard.export.pdf.print

import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.geometry.Offset

fun Canvas.drawText(
    text: String,
    offset: Offset,
    paint: Paint,
) = drawText(text, offset.x, offset.y, paint)