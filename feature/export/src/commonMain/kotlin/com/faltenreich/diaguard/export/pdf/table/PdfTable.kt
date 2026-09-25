package com.faltenreich.diaguard.export.pdf.table

import com.faltenreich.diaguard.export.pdf.PdfBackground
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfRectangle
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfTable(
    private val date: PdfDrawable,
    private val width: Float,
    private val categories: List<Category>,
    private val notes: PdfDrawable,
) : PdfDrawable {

    data class Category(
        val properties: List<Property>,
    ) {

        data class Property(
            val property: PdfDrawable,
            val values: List<Value>,
        ) {

            data class Value(
                val hour: Int,
                val value: PdfDrawable?,
            )
        }
    }

    override fun getSize(): PdfSize {
        return PdfSize(
            width = width,
            height = date.getSize().height + categories.sumOf { category ->
                category.properties.sumOf { property ->
                    property.property.getSize().height.toDouble()
                }
            }.toFloat() + notes.getSize().height,
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        date.drawOn(page, position)
        drawValues(page, position.copy(y = position.y + date.getSize().height))
        notes.drawOn(
            page,
            position.copy(y = position.y + getSize().height - notes.getSize().height)
        )
    }

    private fun drawValues(page: PdfPage, position: PdfPosition) {
        var position = position
        categories.forEachIndexed { categoryIndex, category ->
            category.properties.forEach { property ->
                val height = property.property.getSize().height

                if (categoryIndex % 2 == 0) {
                    drawBackground(
                        page = page,
                        rectangle = PdfRectangle(
                            left = position.x,
                            top = position.y,
                            right = page.viewport.right,
                            bottom = position.y + height,
                        ),
                    )
                }
                property.property.drawOn(page, position)

                drawValues(
                    page = page,
                    position = PdfPosition(x = position.x + DAY_WIDTH, y = position.y),
                    property = property,
                )
                position = position.copy(y = position.y + height)
            }
        }
    }

    private fun drawValues(
        page: PdfPage,
        position: PdfPosition,
        property: Category.Property,
    ) {
        val hoursWidth = page.viewport.right - position.x
        val hourWidth = hoursWidth / HOURS.count()
        property.values.forEach { (hour, value) ->
            if (value != null) {
                val index = hour / HOURS.step
                val x = position.x + (index * hourWidth) + hourWidth / 2 - value.getSize().width / 2
                val y = position.y
                value.drawOn(page, PdfPosition(x, y))
            }
        }
    }

    private fun drawBackground(page: PdfPage, rectangle: PdfRectangle) {
        val background = PdfBackground(rectangle.size, PdfPaint.background)
        background.drawOn(page, rectangle.position)
    }

    private companion object {

        const val DAY_WIDTH = 100f
        const val DAY_HOURS = 24
        const val DAY_STEP = 2
        val HOURS = 0..<DAY_HOURS step DAY_STEP
    }
}