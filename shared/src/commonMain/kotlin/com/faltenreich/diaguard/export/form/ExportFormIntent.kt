package com.faltenreich.diaguard.export.form

import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.export.ExportType
import com.faltenreich.diaguard.export.pdf.PdfLayout

sealed interface ExportFormIntent {

    data class SetDateRange(val dateRange: DateRange) : ExportFormIntent

    data class SelectType(val type: ExportType) : ExportFormIntent

    data class SelectLayout(val layout: PdfLayout) : ExportFormIntent

    data class SetIncludeCalendarWeek(val includeCalendarWeek: Boolean) : ExportFormIntent

    data class SetIncludeDateOfExport(val includeDateOfExport: Boolean) : ExportFormIntent

    data class SetIncludePageNumber(val includePageNumber: Boolean) : ExportFormIntent

    data class SetIncludeNotes(val includeNotes: Boolean) : ExportFormIntent

    data class SetIncludeTags(val includeTags: Boolean) : ExportFormIntent

    data class SetIncludeDaysWithoutEntries(val includeDaysWithoutEntries: Boolean) : ExportFormIntent

    data class SetCategory(val category: ExportFormMeasurementCategory) : ExportFormIntent

    data object Submit : ExportFormIntent
}