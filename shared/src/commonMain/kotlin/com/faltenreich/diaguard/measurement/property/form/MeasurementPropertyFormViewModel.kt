package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.category.form.UpdateMeasurementCategoryUseCase
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListScreen
import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestion
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.navigation.screen.PopScreenUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.measurement_unit_factor_description
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MeasurementPropertyFormViewModel(
    propertyId: Long,
    getPropertyByIdUseCase: GetMeasurementPropertyBdIdUseCase = inject(),
    getUnitSuggestions: GetMeasurementUnitSuggestionsUseCase = inject(),
    getPreference: GetPreferenceUseCase = inject(),
    private val updateProperty: UpdateMeasurementPropertyUseCase = inject(),
    private val deleteProperty: DeleteMeasurementPropertyUseCase = inject(),
    private val updateCategory: UpdateMeasurementCategoryUseCase = inject(),
    private val pushScreen: PushScreenUseCase = inject(),
    private val popScreen: PopScreenUseCase = inject(),
    private val localization: Localization = inject(),
    private val numberFormatter: NumberFormatter = inject(),
) : ViewModel<MeasurementPropertyFormState, MeasurementPropertyFormIntent, Unit>() {

    private val property = checkNotNull(getPropertyByIdUseCase(propertyId))
    private val unit = MutableStateFlow(property.unit)

    var propertyName = MutableStateFlow(property.name)
    var aggregationStyle = MutableStateFlow(property.aggregationStyle)
    // TODO: Format via MeasurementValueMapper
    var valueRangeMinimum = MutableStateFlow(property.range.minimum.toString())
    var valueRangeLow = MutableStateFlow(property.range.low?.toString() ?: "")
    var valueRangeTarget = MutableStateFlow(property.range.target?.toString() ?: "")
    var valueRangeHigh = MutableStateFlow(property.range.high?.toString() ?: "")
    var valueRangeMaximum = MutableStateFlow(property.range.maximum.toString())
    var isValueRangeHighlighted = MutableStateFlow(property.range.isHighlighted)

    private val unitSuggestions = combine(
        unit,
        getUnitSuggestions(propertyId),
        getPreference(DecimalPlacesPreference),
    ) { unit, unitSuggestions, decimalPlaces ->
        if (property.isUserGenerated) emptyList() else unitSuggestions.map { unitSuggestion ->
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
                                locale = localization.getLocale(),
                            ),
                            unitSuggestions.first(MeasurementUnitSuggestion::isDefault).unit.name,
                        )
                    },
                isSelected = unit.id == unitSuggestion.unit.id,
            )
        }
    }

    private val deleteDialog = MutableStateFlow<MeasurementPropertyFormState.DeleteDialog?>(null)
    private val alertDialog = MutableStateFlow<MeasurementPropertyFormState.AlertDialog?>(null)

    override val state = combine(
        flowOf(property),
        unit,
        unitSuggestions,
        deleteDialog,
        alertDialog,
        ::MeasurementPropertyFormState,
    )

    override suspend fun handleIntent(intent: MeasurementPropertyFormIntent) {
        when (intent) {
            is MeasurementPropertyFormIntent.UpdateProperty ->
                updateProperty()
            is MeasurementPropertyFormIntent.OpenDeleteDialog ->
                deleteDialog.update { MeasurementPropertyFormState.DeleteDialog }
            is MeasurementPropertyFormIntent.CloseDeleteDialog ->
                deleteDialog.update { null }
            is MeasurementPropertyFormIntent.OpenAlertDialog ->
                alertDialog.update { MeasurementPropertyFormState.AlertDialog }
            is MeasurementPropertyFormIntent.CloseAlertDialog ->
                alertDialog.update { null }
            is MeasurementPropertyFormIntent.Delete ->
                deleteProperty(intent)
            is MeasurementPropertyFormIntent.OpenUnitSearch ->
                // TODO: Pass mode for selection
                pushScreen(MeasurementUnitListScreen)
            is MeasurementPropertyFormIntent.SelectUnit ->
                selectUnit(intent.unit)
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
                unit = unit.value,
            )
        )

        updateCategory(property.category)

        popScreen()
    }

    private fun deleteProperty(intent: MeasurementPropertyFormIntent.Delete) = scope.launch {
        if (property.isUserGenerated) {
            if (intent.needsConfirmation) {
                deleteDialog.update { MeasurementPropertyFormState.DeleteDialog }
            } else {
                deleteProperty(property)
                popScreen()
            }
        } else {
            alertDialog.update { MeasurementPropertyFormState.AlertDialog }
        }
    }

    private fun selectUnit(unit: MeasurementUnit.Local) {
        this.unit.value = unit
    }
}