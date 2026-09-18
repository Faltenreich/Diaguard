package com.faltenreich.diaguard.export.pdf

internal class PdfDay(text: String) : PdfDrawable {

    private val day = PdfText(text, PdfPaint.bold)
    private val padding = PdfSpacing.P_4.points

    override fun getSize(page: PdfPage): PdfSize {
        return PdfSize(
            width = page.viewport.width,
            height = day.getSize(page).height + padding * 2,
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        val dayPosition = position.copy(x = position.x + padding, y = position.y + padding)
        day.drawOn(page, dayPosition)

        val hourPosition = PdfPosition(x = dayPosition.x + DAY_WIDTH, y = dayPosition.y)
        drawHours(page, hourPosition)
    }

    private fun drawHours(page: PdfPage, position: PdfPosition) {
        val progression = 0..DAY_HOURS step DAY_STEP
        val hoursWidth = page.viewport.right - position.x
        val hourWidth = hoursWidth / progression.count()
        for (hour in progression) {
            val index = hour / progression.step
            val text = PdfText(hour.toString(), PdfPaint.normal)
            val x = position.x + (index * hourWidth) + hourWidth / 2 - text.getSize(page).width / 2
            val y = position.y
            text.drawOn(page, PdfPosition(x, y))
        }
    }

    private companion object {

        const val DAY_WIDTH = 100f
        const val DAY_HOURS = 24
        const val DAY_STEP = 2
    }
}