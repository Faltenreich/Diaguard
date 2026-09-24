package com.faltenreich.diaguard.export.pdf.table

import com.faltenreich.diaguard.persistence.pdf.PdfDrawable

internal data class PdfTableData(
    val categories: List<Category>,
) {

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