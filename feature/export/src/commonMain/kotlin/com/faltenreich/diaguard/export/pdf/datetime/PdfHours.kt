package com.faltenreich.diaguard.export.pdf.datetime

import com.faltenreich.diaguard.export.pdf.PdfText
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfHours(private val size: PdfSize) : PdfDrawable {

    override fun getSize(): PdfSize {
        return size
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        val progression = PROGRESSION
        val hoursWidth = page.viewport.right - position.x
        val hourWidth = hoursWidth / progression.count()
        for (hour in progression) {
            val index = hour / progression.step
            val text = PdfText(hour.toString(), PdfPaint.label)
            val x = position.x + (index * hourWidth) + hourWidth / 2 - text.getSize().width / 2
            val y = position.y
            text.drawOn(page, PdfPosition(x, y))
        }
    }

    private companion object {

        const val COUNT = 24
        const val STEP = 2
        val PROGRESSION = 0..<COUNT step STEP
    }
}