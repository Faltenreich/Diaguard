package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListItemState
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.measurement_unit_factor_description

class MeasurementPropertyFormStateMapper(
    private val localization: Localization,
    private val numberFormatter: NumberFormatter,
) {

    operator fun invoke(
        property: MeasurementProperty.Local,
        units: List<MeasurementUnit.Local>,
    ): MeasurementPropertyFormState {
        val defaultUnit = units.first(MeasurementUnit::isDefault)
        return MeasurementPropertyFormState(
            property = property,
            units = if (property.isUserGenerated) emptyList() else units.map { unit ->
                MeasurementUnitListItemState(
                    unit = unit,
                    title = unit.name,
                    subtitle = unit.takeIf(MeasurementUnit::isDefault)?.run {
                        localization.getString(
                            Res.string.measurement_unit_factor_description,
                            numberFormatter(unit.factor),
                            defaultUnit.name,
                        )
                    },
                    isSelected = property.selectedUnit.id == unit.id,
                )
            }
        )
    }
}