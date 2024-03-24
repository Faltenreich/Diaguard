package com.faltenreich.diaguard.export

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.export.pdf.PdfLayout
import com.faltenreich.diaguard.measurement.property.list.GetMeasurementPropertiesUseCase
import com.faltenreich.diaguard.shared.architecture.FormViewModel
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExportFormViewModel(
    getToday: GetTodayUseCase = inject(),
    getMeasurementProperties: GetMeasurementPropertiesUseCase,
    private val export: ExportUseCase,
    private val dateTimeFormatter: DateTimeFormatter,
    private val localization: Localization,
) : FormViewModel<ExportFormIntent>() {

    private val initialDateRange = getToday().let { today ->
        today.minus(1, DateUnit.WEEK) .. today
    }
    var dateRange by mutableStateOf(initialDateRange)
    val dateRangeLocalized: String
        get() = dateTimeFormatter.formatDateRange(dateRange)

    var exportType by mutableStateOf(ExportType.PDF)
    val exportTypeLocalized: String
        get() = localization.getString(exportType.title)
    val exportTypes = listOf(ExportType.PDF, ExportType.CSV)

    // TODO: Read initial values from preferences

    var pdfLayout by mutableStateOf(PdfLayout.TABLE)
    val pdfLayoutLocalized: String
        get() = localization.getString(pdfLayout.title)
    val pdfLayouts = listOf(PdfLayout.TABLE, PdfLayout.TIMELINE, PdfLayout.LOG)
    var includeCalendarWeek by mutableStateOf(true)
    var includeDateOfExport by mutableStateOf(true)
    var includePageNumber by mutableStateOf(true)

    var includeNotes by mutableStateOf(true)
    var includeTags by mutableStateOf(true)
    var includeDaysWithoutEntries by mutableStateOf(true)

    var properties by mutableStateOf(emptyList<ExportFormMeasurementProperty>())

    init {
        scope.launch {
            getMeasurementProperties().collect { properties ->
                val exportProperties = properties.map { property ->
                    ExportFormMeasurementProperty(
                        property = property,
                        isExported = true,
                        isMerged = false,
                    )
                }.sortedBy { it.property.sortIndex }
                withContext(Dispatchers.Main) {
                    this@ExportFormViewModel.properties = exportProperties
                }
            }
        }
    }

    override fun onIntent(intent: ExportFormIntent) {
        when (intent) {
            is ExportFormIntent.SetProperty -> setProperty(intent.property)
            is ExportFormIntent.Submit -> submit()
        }
    }

    fun setProperty(property: ExportFormMeasurementProperty) {
        properties = properties
            .filter { it.property != property.property }
            .plus(property)
            .sortedBy { it.property.sortIndex }
    }

    fun submit() {
        val data = when (exportType) {
            ExportType.PDF -> ExportData.Pdf(
                dateRange = dateRange,
                includeNotes = includeNotes,
                includeTags = includeTags,
                includeDaysWithoutEntries = includeDaysWithoutEntries,
                layout = pdfLayout,
                includeCalendarWeek = includeCalendarWeek,
                includeDateOfExport = includeDateOfExport,
                includePageNumber = includePageNumber,
            )
            ExportType.CSV -> ExportData.Csv(
                dateRange = dateRange,
                includeNotes = includeNotes,
                includeTags = includeTags,
                includeDaysWithoutEntries = includeDaysWithoutEntries,
            )
        }
        export(data)
    }
}