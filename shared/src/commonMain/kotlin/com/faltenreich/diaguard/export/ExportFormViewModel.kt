package com.faltenreich.diaguard.export

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.export.pdf.PdfLayout
import com.faltenreich.diaguard.measurement.property.list.GetMeasurementPropertiesUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.datetime.DateTimeFormatter
import com.faltenreich.diaguard.shared.datetime.DateUnit
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class ExportFormViewModel(
    private val dispatcher: CoroutineDispatcher = inject(),
    val export: ExportUseCase,
    getMeasurementProperties: GetMeasurementPropertiesUseCase,
    dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
    private val localization: Localization,
) : ViewModel() {

    private val initialDateRange = dateTimeFactory.today().let { today ->
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
        viewModelScope.launch(dispatcher) {
            getMeasurementProperties().collect { properties ->
                this@ExportFormViewModel.properties = properties.map { property ->
                    ExportFormMeasurementProperty(
                        property = property,
                        isExported = true,
                        isMerged = false,
                    )
                }.sortedBy { it.property.sortIndex }
            }
        }
    }

    fun setProperty(property: ExportFormMeasurementProperty) {
        properties = properties
            .filter { it.property != property.property }
            .plus(property)
            .sortedBy { it.property.sortIndex }
    }
}