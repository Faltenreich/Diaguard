package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.UpdateMeasurementUnitUseCase
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListItemState
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.navigation.modal.DeleteModal
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.measurement_unit_factor_description
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class MeasurementPropertyFormViewModel(
    property: MeasurementProperty,
    localization: Localization = inject(),
    getMeasurementPropertyUseCase: GetMeasurementPropertyUseCase = inject(),
    private val updateUnit: UpdateMeasurementUnitUseCase = inject(),
    private val updateProperty: UpdateMeasurementPropertyUseCase = inject(),
    private val deleteProperty: DeleteMeasurementPropertyUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
    private val openModal: OpenModalUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
) : ViewModel<MeasurementPropertyFormState, MeasurementPropertyFormIntent, Unit>() {

    var propertyName = MutableStateFlow(property.name)
    var selectedUnit = MutableStateFlow(property.selectedUnit)
    var aggregationStyle = MutableStateFlow(property.aggregationStyle)
    var valueRangeMinimum = MutableStateFlow(property.range.minimum.toString())
    var valueRangeLow = MutableStateFlow(property.range.low?.toString() ?: "")
    var valueRangeTarget = MutableStateFlow(property.range.target?.toString() ?: "")
    var valueRangeHigh = MutableStateFlow(property.range.high?.toString() ?: "")
    var valueRangeMaximum = MutableStateFlow(property.range.maximum.toString())
    var isValueRangeHighlighted = MutableStateFlow(property.range.isHighlighted)

    var unitName = MutableStateFlow(property.selectedUnit.name)

    override val state = combine(
        getMeasurementPropertyUseCase(property),
        selectedUnit,
        unitName,
    ) { property, selectedUnit, unitName ->
        MeasurementPropertyFormState(
            property = property,
            unitName = if (property.isUserGenerated) unitName else property.selectedUnit.abbreviation,
            units = if (property.isUserGenerated) emptyList() else property.units.map { unit ->
                MeasurementUnitListItemState(
                    unit = unit,
                    title = unit.name,
                    subtitle = unit.takeIf(MeasurementUnit::isDefault)?.run {
                        localization.getString(
                            Res.string.measurement_unit_factor_description,
                            unit.factor.toString(), // TODO: Format
                            unit.property.units.first(MeasurementUnit::isDefault).name,
                        )
                    },
                    isSelected = selectedUnit.id == unit.id,
                )
            }
        )
    }

    override fun handleIntent(intent: MeasurementPropertyFormIntent) {
        when (intent) {
            is MeasurementPropertyFormIntent.UpdateProperty -> updateProperty()
            is MeasurementPropertyFormIntent.DeleteProperty -> deleteProperty()
            is MeasurementPropertyFormIntent.SelectUnit -> selectUnit(intent.unit)
        }
    }

    private fun updateProperty() {
        val property = stateInScope.value?.property ?: return

        updateUnit(property.selectedUnit.copy(name = unitName.value))

        updateProperty(
            property.copy(
                name = propertyName.value,
                aggregationStyle = aggregationStyle.value,
                range = MeasurementValueRange(
                    minimum = valueRangeMinimum.value.toDouble(),
                    low = valueRangeLow.value.toDoubleOrNull(),
                    target = valueRangeLow.value.toDoubleOrNull(),
                    high = valueRangeHigh.value.toDoubleOrNull(),
                    maximum = valueRangeMaximum.value.toDouble(),
                    isHighlighted = isValueRangeHighlighted.value,
                ),
                selectedUnitId = selectedUnit.value.id,
            )
        )
        navigateBack()
    }

    private fun deleteProperty() {
        val property = stateInScope.value?.property ?: return
        openModal(
            DeleteModal(
                onDismissRequest = closeModal::invoke,
                onConfirmRequest = {
                    deleteProperty(property)
                    closeModal()
                    navigateBack()
                }
            )
        )
    }

    private fun selectUnit(unit: MeasurementUnit) {
        selectedUnit.value = unit
    }
}