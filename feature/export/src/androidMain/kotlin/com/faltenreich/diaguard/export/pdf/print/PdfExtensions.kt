package com.faltenreich.diaguard.export.pdf.print

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

fun Canvas.drawText(
    text: String,
    position: PointF,
    paint: Paint,
) = drawText(text, position.x, position.y, paint)