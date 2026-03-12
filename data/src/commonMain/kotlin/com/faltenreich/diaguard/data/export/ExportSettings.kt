package com.faltenreich.diaguard.data.export

import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty

data class ExportSettings(
    val categories: List<Category>,
    val exportType: ExportType,
    val includeCalendarWeek: Boolean,
    val includeDateOfExport: Boolean,
    val includeDaysWithoutEntries: Boolean,
    val includeNotes: Boolean,
    val includePageNumber: Boolean,
    val includeTags: Boolean,
    val includeFoodEaten: Boolean,
    val pdfLayout: PdfLayout,
) {

    data class Category(
        val category: MeasurementCategory.Local,
        val isExported: Boolean,
        val properties: List<Property>,
    ) {

        data class Property(
            val property: MeasurementProperty.Local,
            val isExported: Boolean,
        )
    }
}