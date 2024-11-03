package com.faltenreich.diaguard.export.form

import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.export.ExportType
import com.faltenreich.diaguard.export.pdf.PdfLayout

data class ExportFormState(
    val date: Date,
    val type: Type,
    val layout: Layout,
    val content: Content,
) {

    data class Date(
        val dateRange: DateRange,
        val dateRangeLocalized: String,
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
        val categories: List<ExportFormMeasurementCategory>,
        val includeNotes: Boolean,
        val includeTags: Boolean,
    )
}