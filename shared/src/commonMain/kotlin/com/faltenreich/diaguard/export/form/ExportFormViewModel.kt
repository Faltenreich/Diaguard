package com.faltenreich.diaguard.export.form

import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.export.ExportData
import com.faltenreich.diaguard.export.ExportType
import com.faltenreich.diaguard.export.ExportUseCase
import com.faltenreich.diaguard.export.pdf.PdfLayout
import com.faltenreich.diaguard.measurement.category.GetActiveMeasurementCategoriesUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExportFormViewModel(
    getToday: GetTodayUseCase,
    getCategories: GetActiveMeasurementCategoriesUseCase,
    private val export: ExportUseCase,
    private val dateTimeFormatter: DateTimeFormatter,
) : ViewModel<ExportFormState, ExportFormIntent, Unit>() {

    private val initialDateRange = getToday().let { today ->
        today.minus(1, DateUnit.WEEK) .. today
    }

    private val dateRange = MutableStateFlow(initialDateRange)
    private val dateRangeLocalized = dateRange.map(dateTimeFormatter::formatDateRange)

    private val exportTypes = listOf(ExportType.PDF, ExportType.CSV)
    private var exportTypeSelected = MutableStateFlow(exportTypes.first())

    private val pdfLayouts = listOf(PdfLayout.TABLE, PdfLayout.TIMELINE, PdfLayout.LOG)
    private var pdfLayoutSelected = MutableStateFlow(pdfLayouts.first())

    private val includeCalendarWeek = MutableStateFlow(true)
    private val includeDateOfExport = MutableStateFlow(true)
    private val includePageNumber = MutableStateFlow(true)
    private val includeNotes = MutableStateFlow(true)
    private val includeTags = MutableStateFlow(true)
    private val includeDaysWithoutEntries = MutableStateFlow(true)

    private val categories = MutableStateFlow(emptyList<ExportFormMeasurementCategory>())

    // TODO: Read initial values from preferences
    override val state = combine(
        combine(
            dateRange,
            dateRangeLocalized,
            includeCalendarWeek,
            includeDateOfExport,
            ExportFormState::Date,
        ),
        combine(
            exportTypeSelected,
            flowOf(exportTypes),
            ExportFormState::Type,
        ),
        combine(
            pdfLayoutSelected,
            flowOf(pdfLayouts),
            includePageNumber,
            includeDaysWithoutEntries,
            ExportFormState::Layout,
        ),
        combine(
            categories,
            includeNotes,
            includeTags,
            ExportFormState::Content,
        ),
        ::ExportFormState,
    )

    init {
        scope.launch {
            getCategories().collectLatest { categories ->
                val exportCategories = categories.map { category ->
                    ExportFormMeasurementCategory(
                        category = category,
                        isExported = true,
                        isMerged = false,
                    )
                }.sortedBy { it.category.sortIndex }
                this@ExportFormViewModel.categories.update { exportCategories }
            }
        }
    }

    override suspend fun handleIntent(intent: ExportFormIntent) {
        when (intent) {
            is ExportFormIntent.SetDateRange -> dateRange.update { intent.dateRange }
            is ExportFormIntent.SelectType -> exportTypeSelected.update { intent.type }
            is ExportFormIntent.SelectLayout -> pdfLayoutSelected.update { intent.layout }
            is ExportFormIntent.SetIncludeCalendarWeek -> includeCalendarWeek.update { intent.includeCalendarWeek }
            is ExportFormIntent.SetIncludeDateOfExport -> includeDateOfExport.update { intent.includeDateOfExport }
            is ExportFormIntent.SetIncludePageNumber -> includePageNumber.update { intent.includePageNumber }
            is ExportFormIntent.SetIncludeNotes -> includeNotes.update { intent.includeNotes }
            is ExportFormIntent.SetIncludeTags -> includeTags.update { intent.includeTags }
            is ExportFormIntent.SetIncludeDaysWithoutEntries ->
                includeDaysWithoutEntries.update { intent.includeDaysWithoutEntries }
            is ExportFormIntent.SetCategory -> updateCategory(intent.category)
            is ExportFormIntent.Submit -> submit()
        }
    }

    private fun updateCategory(update: ExportFormMeasurementCategory) {
        categories.update { categories ->
            categories.map { category ->
                if (category.category == update.category) update
                else category
            }
        }
    }

    fun submit() {
        val data = when (exportTypeSelected.value) {
            ExportType.PDF -> ExportData.Pdf(
                dateRange = dateRange.value,
                includeNotes = includeNotes.value,
                includeTags = includeTags.value,
                includeDaysWithoutEntries = includeDaysWithoutEntries.value,
                layout = pdfLayoutSelected.value,
                includeCalendarWeek = includeCalendarWeek.value,
                includeDateOfExport = includeDateOfExport.value,
                includePageNumber = includePageNumber.value,
            )
            ExportType.CSV -> ExportData.Csv(
                dateRange = dateRange.value,
                includeNotes = includeNotes.value,
                includeTags = includeTags.value,
                includeDaysWithoutEntries = includeDaysWithoutEntries.value,
            )
        }
        export(data)
    }
}