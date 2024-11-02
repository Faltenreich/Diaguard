package com.faltenreich.diaguard.export

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.export.pdf.PdfLayout
import com.faltenreich.diaguard.measurement.category.GetActiveMeasurementCategoriesUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.localization.Localization
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExportFormViewModel(
    getToday: GetTodayUseCase,
    getCategories: GetActiveMeasurementCategoriesUseCase,
    private val export: ExportUseCase,
    private val dateTimeFormatter: DateTimeFormatter,
    private val localization: Localization,
) : ViewModel<Unit, ExportFormIntent, Unit>() {

    override val state = emptyFlow<Unit>()

    private val initialDateRange = getToday().let { today ->
        today.minus(1, DateUnit.WEEK) .. today
    }
    var dateRange by mutableStateOf(initialDateRange)
    val dateRangeLocalized: String
        get() = dateTimeFormatter.formatDateRange(dateRange)

    var exportType by mutableStateOf(ExportType.PDF)
    val exportPropertyLocalized: String
        get() = localization.getString(exportType.title)
    val exportProperties = listOf(ExportType.PDF, ExportType.CSV)

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

    var categories by mutableStateOf(emptyList<ExportFormMeasurementCategory>())

    init {
        scope.launch {
            getCategories().collect { categories ->
                val exportCategories = categories.map { category ->
                    ExportFormMeasurementCategory(
                        category = category,
                        isExported = true,
                        isMerged = false,
                    )
                }.sortedBy { it.category.sortIndex }
                withContext(Dispatchers.Main) {
                    this@ExportFormViewModel.categories = exportCategories
                }
            }
        }
    }

    override suspend fun handleIntent(intent: ExportFormIntent) {
        when (intent) {
            is ExportFormIntent.SetCategory -> setCategory(intent.category)
            is ExportFormIntent.Submit -> submit()
        }
    }

    fun setCategory(category: ExportFormMeasurementCategory) {
        categories = categories
            .filter { it.category != category.category }
            .plus(category)
            .sortedBy { it.category.sortIndex }
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