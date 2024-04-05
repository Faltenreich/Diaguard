package com.faltenreich.diaguard.measurement.type.form

sealed interface MeasurementTypeFormIntent {

    data class EditTypeName(val input: String) : MeasurementTypeFormIntent

    data class EditUnitName(val input: String) : MeasurementTypeFormIntent

    data class EditValueRangeMinimum(val input: String) : MeasurementTypeFormIntent

    data class EditValueRangeLow(val input: String) : MeasurementTypeFormIntent

    data class EditValueRangeTarget(val input: String) : MeasurementTypeFormIntent

    data class EditValueRangeHigh(val input: String) : MeasurementTypeFormIntent

    data class EditValueRangeMaximum(val input: String) : MeasurementTypeFormIntent

    data class EditIsValueRangeHighlighted(val input: Boolean) : MeasurementTypeFormIntent

    data object UpdateType : MeasurementTypeFormIntent

    data object DeleteType : MeasurementTypeFormIntent
}