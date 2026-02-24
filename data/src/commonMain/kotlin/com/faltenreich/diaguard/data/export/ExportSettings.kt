package com.faltenreich.diaguard.data.export

import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty

data class ExportSettings(
    val date: Date,
    val type: Type,
    val layout: Layout,
    val content: Content,
) {

    data class Date(
        val includeCalendarWeek: Boolean,
        val includeDateOfExport: Boolean,
    )

    data class Type(
        val selection: ExportType,
        val options: List<ExportType>,
    )

    data class Layout(
        val selection: PdfLayout,
        val options: List<PdfLayout>,
        val includePageNumber: Boolean,
        val includeDaysWithoutEntries: Boolean,
    )

    data class Content(
        val categories: List<Category>,
        val includeNotes: Boolean,
        val includeTags: Boolean,
    ) {

        data class Category(
            val category: MeasurementCategory,
            val isExported: Boolean,
            val properties: List<Property>,
        ) {

            data class Property(
                val property: MeasurementProperty,
                val isExported: Boolean,
            )
        }
    }
}