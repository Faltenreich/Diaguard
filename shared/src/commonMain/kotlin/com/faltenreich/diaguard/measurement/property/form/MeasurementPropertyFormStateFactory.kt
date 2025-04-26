package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestion
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.localization.NumberFormatter
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.measurement_unit_factor_description

class MeasurementPropertyFormStateFactory(
    private val numberFormatter: NumberFormatter,
    private val localization: Localization,
) {

    operator fun invoke(
        property: MeasurementProperty,
        unitSuggestions: List<MeasurementUnitSuggestion.Local>,
        decimalPlaces: Int,
        errorBar: MeasurementPropertyFormState.ErrorBar?,
        deleteDialog: MeasurementPropertyFormState.DeleteDialog?,
        alertDialog: MeasurementPropertyFormState.AlertDialog?,
    ): MeasurementPropertyFormState {
        return MeasurementPropertyFormState(
            property = property,
            valueRange = getValueRange(property, decimalPlaces),
            unitSuggestions = getUnitSuggestions(unitSuggestions, property, decimalPlaces),
            errorBar = errorBar,
            deleteDialog = deleteDialog,
            alertDialog = alertDialog,
        )
    }

    private fun getValueRange(
        property: MeasurementProperty,
        decimalPlaces: Int,
    ): MeasurementPropertyFormState.ValueRange {
        return MeasurementPropertyFormState.ValueRange(
            minimum = property.range.minimum.let { number ->
                numberFormatter(
                    number = number,
                    scale = decimalPlaces,
                )
            },
            low = property.range.low?.let { number ->
                numberFormatter(
                    number = number,
                    scale = decimalPlaces,
                )
            } ?: "",
            target = property.range.target?.let { number ->
                numberFormatter(
                    number = number,
                    scale = decimalPlaces,
                )
            } ?: "",
            high = property.range.high?.let { number ->
                numberFormatter(
                    number = number,
                    scale = decimalPlaces,
                )
            } ?: "",
            maximum = property.range.maximum.let { number ->
                numberFormatter(
                    number = number,
                    scale = decimalPlaces,
                )
            },
            isHighlighted = property.range.isHighlighted,
            unit = property.unit?.abbreviation,
        )
    }

    private fun getUnitSuggestions(
        unitSuggestions: List<MeasurementUnitSuggestion.Local>,
        property: MeasurementProperty,
        decimalPlaces: Int,
    ): List<MeasurementPropertyFormState.UnitSuggestion> {
        return unitSuggestions.map { unitSuggestion ->
            MeasurementPropertyFormState.UnitSuggestion(
                unit = unitSuggestion.unit,
                title = unitSuggestion.unit.name,
                subtitle = unitSuggestion.takeUnless(MeasurementUnitSuggestion::isDefault)
                    ?.run {
                        localization.getString(
                            Res.string.measurement_unit_factor_description,
                            numberFormatter(
                                number = unitSuggestion.factor,
                                scale = decimalPlaces,
                            ),
                            unitSuggestions.first(MeasurementUnitSuggestion::isDefault).unit.name,
                        )
                    },
                isSelected = property.unit?.id == unitSuggestion.unit.id,
            )
        }
    }
}