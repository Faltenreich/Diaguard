package com.faltenreich.diaguard.export.pdf.table

import com.faltenreich.diaguard.export.pdf.PdfBackground
import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfPage
import com.faltenreich.diaguard.persistence.pdf.PdfPaint
import com.faltenreich.diaguard.persistence.pdf.PdfPosition
import com.faltenreich.diaguard.persistence.pdf.PdfRectangle
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal class PdfTableCategories(
    private val width: Float,
    private val categories: List<Category>,
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
            height = categories.sumOf { category ->
                category.properties.sumOf { property ->
                    property.property.getSize().height.toDouble()
                }
            }.toFloat(),
        )
    }

    override fun drawOn(page: PdfPage, position: PdfPosition) {
        var position = position
        categories.forEachIndexed { categoryIndex, category ->
            category.properties.forEach { property ->
                val height = property.property.getSize().height

                if (categoryIndex % 2 == 0) {
                    val rectangle = PdfRectangle(
                        left = position.x,
                        top = position.y,
                        right = page.viewport.right,
                        bottom = position.y + height,
                    )
                    val background = PdfBackground(rectangle.size, PdfPaint.background)
                    background.drawOn(page, rectangle.position)
                }
                property.property.drawOn(page, position)

                val hourPosition = position.copy(x = position.x + DAY_WIDTH)
                val hoursWidth = page.viewport.right - hourPosition.x
                val hourWidth = hoursWidth / HOURS.count()
                property.values.forEach { (hour, value) ->
                    if (value != null) {
                        val index = hour / HOURS.step
                        val x =
                            hourPosition.x + (index * hourWidth) + hourWidth / 2 - value.getSize().width / 2
                        val y = hourPosition.y
                        value.drawOn(page, PdfPosition(x, y))
                    }
                }
                position = position.copy(y = position.y + height)
            }
        }
    }

    private companion object {

        const val DAY_WIDTH = 100f
        const val DAY_HOURS = 24
        const val DAY_STEP = 2
        val HOURS = 0..<DAY_HOURS step DAY_STEP
    }
}