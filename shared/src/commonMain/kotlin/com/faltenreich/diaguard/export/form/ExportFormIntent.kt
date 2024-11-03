package com.faltenreich.diaguard.export.form

sealed interface ExportFormIntent {

    data class SetCategory(val category: ExportFormMeasurementCategory) : ExportFormIntent

    data object Submit : ExportFormIntent
}