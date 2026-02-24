package com.faltenreich.diaguard.export.form

import com.faltenreich.diaguard.data.export.ExportSettings
import com.faltenreich.diaguard.datetime.DateRange

internal sealed interface ExportFormIntent {

    data class SetDateRange(val dateRange: DateRange) : ExportFormIntent

    data class SetSettings(val settings: ExportSettings) : ExportFormIntent

    data object Submit : ExportFormIntent
}