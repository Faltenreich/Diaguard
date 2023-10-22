package com.faltenreich.diaguard.export

import androidx.compose.runtime.mutableStateOf
import com.faltenreich.diaguard.export.pdf.PdfLayout
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.datetime.DateTimeFormatter
import com.faltenreich.diaguard.shared.datetime.DateUnit
import com.faltenreich.diaguard.shared.localization.Localization

class ExportFormViewModel(
    val export: ExportUseCase,
    dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
    private val localization: Localization,
) : ViewModel() {

    private val initialDateRange = dateTimeFactory.today().let { today ->
        today.minus(1, DateUnit.WEEK) .. today
    }
    var dateRange = mutableStateOf(initialDateRange)
    val dateRangeLocalized: String
        get() = dateTimeFormatter.formatDateRange(dateRange.value)

    var exportType = mutableStateOf(ExportType.PDF)
    val exportTypeLocalized: String
        get() = localization.getString(exportType.value.title)
    val exportTypes = listOf(ExportType.PDF, ExportType.CSV)

    // TODO: Read from preferences
    var pdfLayout = mutableStateOf(PdfLayout.TABLE)
    val pdfLayoutLocalized: String
        get() = localization.getString(pdfLayout.value.title)
    val pdfLayouts = listOf(PdfLayout.TABLE, PdfLayout.TIMELINE, PdfLayout.LOG)
    var includeCalendarWeek = mutableStateOf(true)
    var includeDateOfExport = mutableStateOf(true)
    var includePageNumber = mutableStateOf(true)

    var includeNotes = mutableStateOf(true)
    var includeTags = mutableStateOf(true)
    var includeDaysWithoutEntries = mutableStateOf(true)
}