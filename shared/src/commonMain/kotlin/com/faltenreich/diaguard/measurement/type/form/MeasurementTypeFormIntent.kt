package com.faltenreich.diaguard.measurement.type.form

import com.faltenreich.diaguard.measurement.type.MeasurementType

sealed interface MeasurementTypeFormIntent {

    data class EditTypeName(val input: String) : MeasurementTypeFormIntent

    data class EditUnitName(val input: String) : MeasurementTypeFormIntent

    data class EditValueRangeMinimum(val input: String) : MeasurementTypeFormIntent

    data class EditValueRangeLow(val input: String) : MeasurementTypeFormIntent

    data class EditValueRangeTarget(val input: String) : MeasurementTypeFormIntent

    data class EditValueRangeHigh(val input: String) : MeasurementTypeFormIntent

    data class EditValueRangeMaximum(val input: String) : MeasurementTypeFormIntent

    data class EditIsValueRangeHighlighted(val input: Boolean) : MeasurementTypeFormIntent

    data object ShowDeletionDialog : MeasurementTypeFormIntent

    data object HideDeletionDialog : MeasurementTypeFormIntent

    data class DeleteType(val type: MeasurementType) : MeasurementTypeFormIntent
}