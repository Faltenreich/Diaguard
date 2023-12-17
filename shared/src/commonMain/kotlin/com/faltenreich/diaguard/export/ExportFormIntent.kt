package com.faltenreich.diaguard.export

sealed interface ExportFormIntent {

    data class SetProperty(val property: ExportFormMeasurementProperty) : ExportFormIntent

    data object Submit : ExportFormIntent
}