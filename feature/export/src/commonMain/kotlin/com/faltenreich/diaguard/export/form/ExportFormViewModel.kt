package com.faltenreich.diaguard.export.form

import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.data.export.PdfLayout
import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.export.ExportUseCase
import com.faltenreich.diaguard.export.GetExportSettingsUseCase
import com.faltenreich.diaguard.export.SetExportSettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ExportFormViewModel(
    getToday: GetTodayUseCase,
    getSettings: GetExportSettingsUseCase,
    private val setSettings: SetExportSettingsUseCase,
    private val setCategory: SetExportCategoryUseCase,
    private val export: ExportUseCase,
    private val dateTimeFormatter: DateTimeFormatter,
) : ViewModel<ExportFormState, ExportFormIntent, Unit>() {

    private val dateRange = MutableStateFlow(getToday().let(::DateRange))
    private val dateRangeLocalized = dateRange.map(dateTimeFormatter::formatDateRange)
    private val exportTypes = listOf(ExportType.PDF, ExportType.CSV)
    private val pdfLayouts = listOf(PdfLayout.TABLE, PdfLayout.TIMELINE, PdfLayout.LOG)

    override val state = combine(
        dateRange,
        dateRangeLocalized,
        flowOf(exportTypes),
        flowOf(pdfLayouts),
        getSettings(),
        ::ExportFormState,
    )

    override suspend fun handleIntent(intent: ExportFormIntent) {
        when (intent) {
            is ExportFormIntent.SetDateRange -> dateRange.update { intent.dateRange }
            is ExportFormIntent.SetSettings -> setSettings(intent.settings)
            is ExportFormIntent.SetCategory -> setCategory(intent.category)
            is ExportFormIntent.Submit -> submit()
        }
    }

    fun submit() = scope.launch {
        val settings = state.first().settings
        export(settings)
    }
}