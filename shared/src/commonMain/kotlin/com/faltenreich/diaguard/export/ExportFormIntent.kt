package com.faltenreich.diaguard.export

sealed interface ExportFormIntent {

    data class SetCategory(val category: ExportFormMeasurementCategory) : ExportFormIntent

    data object Submit : ExportFormIntent
}