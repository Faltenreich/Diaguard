package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit

data class MeasurementPropertyFormState(
    val property: MeasurementProperty,
    val valueRange: ValueRange,
    val unitSuggestions: List<UnitSuggestion>,
    val errorBar: ErrorBar?,
    val dialog: Dialog?,
) {

    data class UnitSuggestion(
        val unit: MeasurementUnit.Local,
        val title: String,
        val subtitle: String?,
        val isSelected: Boolean,
    )

    data class ValueRange(
        val minimum: String,
        val low: String,
        val target: String,
        val high: String,
        val maximum: String,
        val isHighlighted: Boolean,
        val unit: String?,
    )

    data object ErrorBar

    sealed interface Dialog {

        data object Delete : Dialog

        data object Alert : Dialog
    }
}