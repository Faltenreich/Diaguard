package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty

sealed interface MeasurementCategoryFormIntent {

    data class DecrementSortIndex(
        val property: MeasurementProperty.Local,
        val inProperties: List<MeasurementProperty.Local>,
    ) : MeasurementCategoryFormIntent

    data class IncrementSortIndex(
        val property: MeasurementProperty.Local,
        val inProperties: List<MeasurementProperty.Local>,
    ) : MeasurementCategoryFormIntent

    data class EditProperty(val property: MeasurementProperty.Local) : MeasurementCategoryFormIntent

    data object AddProperty : MeasurementCategoryFormIntent

    data object Store : MeasurementCategoryFormIntent

    data class Delete(val needsConfirmation: Boolean) : MeasurementCategoryFormIntent

    data object OpenDeleteDialog : MeasurementCategoryFormIntent

    data object CloseDeleteDialog : MeasurementCategoryFormIntent

    data object OpenAlertDialog : MeasurementCategoryFormIntent

    data object CloseAlertDialog : MeasurementCategoryFormIntent
}