package com.faltenreich.diaguard.measurement.category.form

sealed interface MeasurementCategoryFormIntent {

    data object Store : MeasurementCategoryFormIntent

    data class Delete(val needsConfirmation: Boolean) : MeasurementCategoryFormIntent

    data object OpenDeleteDialog : MeasurementCategoryFormIntent

    data object CloseDeleteDialog : MeasurementCategoryFormIntent

    data object OpenAlertDialog : MeasurementCategoryFormIntent

    data object CloseAlertDialog : MeasurementCategoryFormIntent
}