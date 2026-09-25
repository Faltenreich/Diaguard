package com.faltenreich.diaguard.export.pdf.table

import com.faltenreich.diaguard.persistence.pdf.PdfDrawable
import com.faltenreich.diaguard.persistence.pdf.PdfSize

internal data class PdfTableData(
    val date: PdfDrawable,
    val width: Float,
    val categories: List<Category>,
    val notes: PdfDrawable,
) {

    val size: PdfSize = PdfSize(
        width = width,
        height = date.getSize().height + categories.sumOf { category ->
            category.properties.sumOf { property ->
                property.property.getSize().height.toDouble()
            }
        }.toFloat() + notes.getSize().height,
    )

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
}