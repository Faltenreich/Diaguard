package com.faltenreich.diaguard.export

import androidx.compose.runtime.mutableStateOf
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
    val exportTypeLocalized
        get() = localization.getString(exportType.value.title)
}