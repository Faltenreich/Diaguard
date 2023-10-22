package com.faltenreich.diaguard.export

import com.faltenreich.diaguard.export.pdf.PdfLayout
import com.faltenreich.diaguard.shared.datetime.Date

sealed interface ExportData {

    val dateRange: ClosedRange<Date>
    val includeNotes: Boolean
    val includeTags: Boolean
    val includeDaysWithoutEntries: Boolean

    class Pdf(
        override val dateRange: ClosedRange<Date>,
        override val includeNotes: Boolean,
        override val includeTags: Boolean,
        override val includeDaysWithoutEntries: Boolean,
        val layout: PdfLayout,
        val includeCalendarWeek: Boolean,
        val includeDateOfExport: Boolean,
        val includePageNumber: Boolean,
    ) : ExportData

    class Csv(
        override val dateRange: ClosedRange<Date>,
        override val includeNotes: Boolean,
        override val includeTags: Boolean,
        override val includeDaysWithoutEntries: Boolean,
    ) : ExportData
}