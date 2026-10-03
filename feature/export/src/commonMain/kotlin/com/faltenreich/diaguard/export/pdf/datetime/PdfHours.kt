package com.faltenreich.diaguard.export.pdf.datetime

import com.faltenreich.diaguard.export.pdf.PdfCell
import com.faltenreich.diaguard.export.pdf.PdfText
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfHours(private val width: Float) : PdfDrawable {

    data class Item(
        val hour: Int,
        val drawable: PdfDrawable,
    )

    private val drawables: List<Item> = PROGRESSION.map { hour ->
        Item(
            hour = hour,
            drawable = PdfCell(PdfText(hour.toString(), PdfPaint.label)),
        )
    }

    override fun getSize(): PdfSize {
        return PdfSize(
            width = width,
            height = drawables.maxOf { it.drawable.getSize().height },
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        val progression = PROGRESSION
        val hoursWidth = page.viewport.right - position.x
        val hourWidth = hoursWidth / progression.count()
        drawables.forEach { (hour, drawable) ->
            val index = hour / progression.step
            val x = position.x + (index * hourWidth) - drawable.getSize().width / 2
            val y = position.y
            drawable.drawOn(page, PdfPosition(x, y))
        }
    }

    private companion object {

        const val COUNT = 24
        const val STEP = 2

        // TODO: Add 24:00
        val PROGRESSION = 0..<COUNT step STEP
    }
}