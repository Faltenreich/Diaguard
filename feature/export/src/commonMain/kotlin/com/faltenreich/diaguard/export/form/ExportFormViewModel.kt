package com.faltenreich.diaguard.export.form

import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import com.faltenreich.diaguard.data.export.ExportType
import com.faltenreich.diaguard.datetime.DateRange
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.export.ExportData
import com.faltenreich.diaguard.export.ExportUseCase
import com.faltenreich.diaguard.export.GetExportSettingsUseCase
import com.faltenreich.diaguard.export.SetExportSettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ExportFormViewModel(
    getToday: GetTodayUseCase,
    getSettings: GetExportSettingsUseCase,
    private val setSettings: SetExportSettingsUseCase,
    private val export: ExportUseCase,
    private val dateTimeFormatter: DateTimeFormatter,
) : ViewModel<ExportFormState, ExportFormIntent, Unit>() {

    private val dateRange = MutableStateFlow(getToday().let(::DateRange))
    private val dateRangeLocalized = dateRange.map(dateTimeFormatter::formatDateRange)

    override val state = combine(
        dateRange,
        dateRangeLocalized,
        getSettings(),
        ::ExportFormState,
    )

    override suspend fun handleIntent(intent: ExportFormIntent) {
        when (intent) {
            is ExportFormIntent.SetDateRange -> dateRange.update { intent.dateRange }
            is ExportFormIntent.SetSettings -> setSettings(intent.settings)
            is ExportFormIntent.Submit -> submit()
        }
    }

    fun submit() = scope.launch {
        val settings = state.first().settings
        val data = when (settings.type.selection) {
            ExportType.PDF -> ExportData.Pdf(
                dateRange = dateRange.value,
                includeNotes = settings.content.includeNotes,
                includeTags = settings.content.includeTags,
                includeDaysWithoutEntries = settings.layout.includeDaysWithoutEntries,
                layout = settings.layout.selection,
                includeCalendarWeek = settings.date.includeCalendarWeek,
                includeDateOfExport = settings.date.includeDateOfExport,
                includePageNumber = settings.layout.includePageNumber,
            )
            ExportType.CSV -> ExportData.Csv(
                dateRange = dateRange.value,
                includeNotes = settings.content.includeNotes,
                includeTags = settings.content.includeTags,
                includeDaysWithoutEntries = settings.layout.includeDaysWithoutEntries,
            )
        }
        export(data)
    }
}