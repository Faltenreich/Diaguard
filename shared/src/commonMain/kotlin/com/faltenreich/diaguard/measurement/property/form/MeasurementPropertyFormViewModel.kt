package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.category.form.UpdateMeasurementCategoryUseCase
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListMode
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListScreen
import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestion
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRangeState
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
import kotlinx.coroutines.flow.update

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

    private val property = MutableStateFlow(checkNotNull(getPropertyByIdUseCase(propertyId)))
    private val unit = MutableStateFlow(property.value.unit)
    private val valueRange = combine(
        property,
        unit
    ) { property, unit ->
        MeasurementValueRangeState(
            minimum = property.range.minimum.toString(),
            low = property.range.low?.toString() ?: "",
            target = property.range.target?.toString() ?: "",
            high = property.range.high?.toString() ?: "",
            maximum = property.range.maximum.toString(),
            isHighlighted = property.range.isHighlighted,
            unit = unit.name,
        )
    }

    private val unitSuggestions = combine(
        unit,
        getUnitSuggestions(propertyId),
        getPreference(DecimalPlacesPreference),
    ) { unit, unitSuggestions, decimalPlaces ->
        if (property.value.isUserGenerated) emptyList() else unitSuggestions.map { unitSuggestion ->
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

    override val state = com.faltenreich.diaguard.shared.architecture.combine(
        property,
        valueRange,
        unit,
        unitSuggestions,
        deleteDialog,
        alertDialog,
        ::MeasurementPropertyFormState,
    )

    override suspend fun handleIntent(intent: MeasurementPropertyFormIntent) {
        when (intent) {
            is MeasurementPropertyFormIntent.UpdateProperty ->
                property.update { it.copy(name = intent.name) }
            is MeasurementPropertyFormIntent.UpdateValueRange ->
                property.update {
                    it.copy(
                        range = MeasurementValueRange(
                            minimum = intent.valueRangeState.minimum.toDoubleOrNull() ?: 0.0,
                            low = intent.valueRangeState.low.toDoubleOrNull(),
                            target = intent.valueRangeState.target.toDoubleOrNull(),
                            high = intent.valueRangeState.high.toDoubleOrNull(),
                            maximum = intent.valueRangeState.maximum.toDoubleOrNull() ?: Double.MAX_VALUE,
                            isHighlighted = intent.valueRangeState.isHighlighted,
                        ),
                    )
                }
            is MeasurementPropertyFormIntent.OpenUnitSearch ->
                pushScreen(MeasurementUnitListScreen(mode = MeasurementUnitListMode.FIND))
            is MeasurementPropertyFormIntent.SelectUnit ->
                unit.update { intent.unit }
            is MeasurementPropertyFormIntent.Submit ->
                submit()
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
        }
    }

    // TODO: Validate
    private suspend fun submit() {
        val property = property.value
        updateProperty(property.copy(unit = unit.value))
        updateCategory(property.category)
        popScreen()
    }

    private suspend fun deleteProperty(intent: MeasurementPropertyFormIntent.Delete) {
        val property = property.value
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
}