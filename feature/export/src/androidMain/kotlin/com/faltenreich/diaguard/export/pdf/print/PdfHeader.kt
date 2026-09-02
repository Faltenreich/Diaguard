package com.faltenreich.diaguard.export.pdf.print

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter

internal class PdfHeader(
    private val dateTime: DateTime,
    private val dateTimeFormatter: DateTimeFormatter,
) : PdfPrintable {

    override fun drawOn(canvas: Canvas) {
        canvas.drawText(
            dateTimeFormatter.formatDate(dateTime.date),
            100f,
            100f,
            Paint().apply { color = Color.BLACK })
    }
}