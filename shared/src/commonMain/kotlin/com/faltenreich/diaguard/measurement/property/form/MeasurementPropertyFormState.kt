package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRangeState

data class MeasurementPropertyFormState(
    val property: MeasurementProperty.Local,
    val valueRange: MeasurementValueRangeState,
    val unit: MeasurementUnit.Local,
    val unitSuggestions: List<UnitSuggestion>,
    val deleteDialog: DeleteDialog?,
    val alertDialog: AlertDialog?,
) {

    data class UnitSuggestion(
        val unit: MeasurementUnit.Local,
        val title: String,
        val subtitle: String?,
        val isSelected: Boolean,
    )

    data object DeleteDialog

    data object AlertDialog
}