package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRangeState

data class MeasurementPropertyFormState(
    val property: MeasurementProperty,
    val valueRange: MeasurementValueRangeState,
    val unitSuggestions: List<UnitSuggestion>,
    val errorBar: ErrorBar?,
    val deleteDialog: DeleteDialog?,
    val alertDialog: AlertDialog?,
) {

    data class UnitSuggestion(
        val unit: MeasurementUnit.Local,
        val title: String,
        val subtitle: String?,
        val isSelected: Boolean,
    )

    data object ErrorBar

    data object DeleteDialog

    data object AlertDialog
}