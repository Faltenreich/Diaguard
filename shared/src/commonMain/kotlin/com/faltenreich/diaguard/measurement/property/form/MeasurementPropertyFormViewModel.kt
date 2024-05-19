package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.GetMeasurementUnitsOfPropertyUseCase
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.UpdateMeasurementUnitUseCase
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.navigation.modal.AlertModal
import com.faltenreich.diaguard.navigation.modal.DeleteModal
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.delete_error_property
import diaguard.shared.generated.resources.delete_title
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

class MeasurementPropertyFormViewModel(
    private val property: MeasurementProperty.Local,
    private val localization: Localization = inject(),
    getUnits: GetMeasurementUnitsOfPropertyUseCase = inject(),
    mapState: MeasurementPropertyFormStateMapper = inject(),
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

    var unitName = MutableStateFlow(property.selectedUnit.abbreviation)

    override val state = combine(flowOf(property), getUnits(property), mapState::invoke)

    override fun handleIntent(intent: MeasurementPropertyFormIntent) {
        when (intent) {
            is MeasurementPropertyFormIntent.UpdateProperty -> updateProperty()
            is MeasurementPropertyFormIntent.DeleteProperty -> deleteProperty()
            is MeasurementPropertyFormIntent.SelectUnit -> selectUnit(intent.unit)
        }
    }

    // TODO: Validate
    private fun updateProperty() {
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
                // TODO: selectedUnitId = selectedUnit.value.id,
            )
        )
        navigateBack()
    }

    private fun deleteProperty() {
        if (property.isUserGenerated) {
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
        } else {
            openModal(
                AlertModal(
                    onDismissRequest = closeModal::invoke,
                    title = localization.getString(Res.string.delete_title),
                    text = localization.getString(Res.string.delete_error_property),
                )
            )
        }
    }

    private fun selectUnit(unit: MeasurementUnit.Local) {
        selectedUnit.value = unit
    }
}