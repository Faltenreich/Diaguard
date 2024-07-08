package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.GetMeasurementUnitsOfPropertyUseCase
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.UpdateMeasurementUnitUseCase
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListItemState
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.preference.DecimalPlaces
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import com.faltenreich.diaguard.shared.view.AlertModal
import com.faltenreich.diaguard.shared.view.DeleteModal
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.delete_error_property
import diaguard.shared.generated.resources.delete_title
import diaguard.shared.generated.resources.measurement_unit_factor_description
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

class MeasurementPropertyFormViewModel(
    private val property: MeasurementProperty.Local,
    private val localization: Localization = inject(),
    private val numberFormatter: NumberFormatter = inject(),
    getPreference: GetPreferenceUseCase = inject(),
    getUnits: GetMeasurementUnitsOfPropertyUseCase = inject(),
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
    // TODO: Format via MeasurementValueMapper
    var valueRangeMinimum = MutableStateFlow(property.range.minimum.toString())
    var valueRangeLow = MutableStateFlow(property.range.low?.toString() ?: "")
    var valueRangeTarget = MutableStateFlow(property.range.target?.toString() ?: "")
    var valueRangeHigh = MutableStateFlow(property.range.high?.toString() ?: "")
    var valueRangeMaximum = MutableStateFlow(property.range.maximum.toString())
    var isValueRangeHighlighted = MutableStateFlow(property.range.isHighlighted)

    var unitName = MutableStateFlow(property.selectedUnit.abbreviation)

    private val units = combine(
        getUnits(property),
        selectedUnit,
        getPreference(DecimalPlaces),
    ) { units, selectedUnit, decimalPlaces ->
        if (property.isUserGenerated) emptyList() else units.map { unit ->
            MeasurementUnitListItemState(
                unit = unit,
                title = unit.name,
                subtitle = unit.takeIf(MeasurementUnit::isDefault)
                    ?.run {
                        localization.getString(
                            Res.string.measurement_unit_factor_description,
                            numberFormatter(
                                number = unit.factor,
                                scale = decimalPlaces,
                                locale = localization.getLocale(),
                            ),
                            units.first(MeasurementUnit::isDefault).name,
                        )
                    },
                isSelected = selectedUnit.id == unit.id,
            )
        }
    }

    override val state = combine(
        flowOf(property),
        units,
        ::MeasurementPropertyFormState,
    )

    override suspend fun handleIntent(intent: MeasurementPropertyFormIntent) {
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