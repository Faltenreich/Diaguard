package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.category.form.UpdateMeasurementCategoryUseCase
import com.faltenreich.diaguard.measurement.unit.GetMeasurementUnitsOfPropertyUseCase
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.UpdateMeasurementUnitUseCase
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListItemState
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.navigation.modal.CloseModalUseCase
import com.faltenreich.diaguard.navigation.modal.OpenModalUseCase
import com.faltenreich.diaguard.navigation.screen.PopScreenUseCase
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class MeasurementPropertyFormViewModel(
    propertyId: Long,
    getPropertyByIdUseCase: GetMeasurementPropertyBdIdUseCase = inject(),
    getPreference: GetPreferenceUseCase = inject(),
    getUnits: GetMeasurementUnitsOfPropertyUseCase = inject(),
    private val updateUnit: UpdateMeasurementUnitUseCase = inject(),
    private val updateProperty: UpdateMeasurementPropertyUseCase = inject(),
    private val deleteProperty: DeleteMeasurementPropertyUseCase = inject(),
    private val updateCategory: UpdateMeasurementCategoryUseCase = inject(),
    private val popScreen: PopScreenUseCase = inject(),
    private val openModal: OpenModalUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
    private val localization: Localization = inject(),
    private val numberFormatter: NumberFormatter = inject(),
) : ViewModel<MeasurementPropertyFormState, MeasurementPropertyFormIntent, Unit>() {

    private val property = checkNotNull(getPropertyByIdUseCase(propertyId))

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
        getPreference(DecimalPlacesPreference),
    ) { units, selectedUnit, decimalPlaces ->
        if (property.isUserGenerated) emptyList() else units.map { unit ->
            MeasurementUnitListItemState(
                unit = unit,
                title = unit.name,
                subtitle = unit.takeUnless(MeasurementUnit::isDefault)
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
    private suspend fun updateProperty() {
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
            )
        )

        val units = units.first().map(MeasurementUnitListItemState::unit)
        units.forEach { unit ->
            updateUnit(
                unit.copy(
                    name = if (property.isUserGenerated) unitName.value else unit.name,
                    isSelected = property.isUserGenerated || unit.id == selectedUnit.value.id,
                ),
            )
        }

        updateCategory(property.category)

        popScreen()
    }

    private fun deleteProperty() = scope.launch {
        if (property.isUserGenerated) {
            openModal(
                DeleteModal(
                    onDismissRequest = { scope.launch { closeModal() } },
                    onConfirmRequest = {
                        deleteProperty(property)
                        scope.launch {
                            closeModal()
                            popScreen()
                        }
                    }
                )
            )
        } else {
            openModal(
                AlertModal(
                    onDismissRequest = { scope.launch { closeModal() } },
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