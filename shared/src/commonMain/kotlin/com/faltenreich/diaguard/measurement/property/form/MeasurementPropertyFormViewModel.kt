package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.category.StoreMeasurementCategoryUseCase
import com.faltenreich.diaguard.measurement.category.form.GetMeasurementCategoryByIdUseCase
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.StoreMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.property.aggregationstyle.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.range.MeasurementValueRange
import com.faltenreich.diaguard.measurement.property.range.MeasurementValueRangeState
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListMode
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListScreen
import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestion
import com.faltenreich.diaguard.navigation.screen.PopScreenUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.localization.NumberFormatter
import com.faltenreich.diaguard.shared.result.Result
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.measurement_unit_factor_description
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update

class MeasurementPropertyFormViewModel(
    categoryId: Long,
    propertyId: Long?,
    getPropertyById: GetMeasurementPropertyBdIdUseCase = inject(),
    getMaximumSortIndex: GetMaximumSortIndexUseCase = inject(),
    getCategoryById: GetMeasurementCategoryByIdUseCase = inject(),
    getUnitSuggestions: GetMeasurementUnitSuggestionsUseCase = inject(),
    getPreference: GetPreferenceUseCase = inject(),
    private val mapValueRangeState: MapMeasurementValueRangeStateUseCase = inject(),
    private val storeProperty: StoreMeasurementPropertyUseCase = inject(),
    private val deleteProperty: DeleteMeasurementPropertyUseCase = inject(),
    private val storeCategory: StoreMeasurementCategoryUseCase = inject(),
    private val pushScreen: PushScreenUseCase = inject(),
    private val popScreen: PopScreenUseCase = inject(),
    private val localization: Localization = inject(),
    private val numberFormatter: NumberFormatter = inject(),
) : ViewModel<MeasurementPropertyFormState, MeasurementPropertyFormIntent, Unit>() {

    private val category = checkNotNull(getCategoryById(categoryId))
    private val property = MutableStateFlow(
        propertyId?.let(getPropertyById::invoke)
            ?: MeasurementProperty.User(
                sortIndex = getMaximumSortIndex(categoryId)?.plus(1) ?: 0,
                category = category,
            )
    )
    private val decimalPlaces = getPreference(DecimalPlacesPreference)

    private val unitSuggestions = property.flatMapLatest { property ->
        when (property) {
            is MeasurementProperty.Local -> combine(
                getUnitSuggestions(property),
                decimalPlaces,
            ) { unitSuggestions, decimalPlaces ->
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
                                    ),
                                    unitSuggestions.first(MeasurementUnitSuggestion::isDefault).unit.name,
                                )
                            },
                        isSelected = property.unit.id == unitSuggestion.unit.id,
                    )
                }
            }
            else -> flowOf(emptyList())
        }

    }

    private val errorBar = MutableStateFlow<MeasurementPropertyFormState.ErrorBar?>(null)
    private val deleteDialog = MutableStateFlow<MeasurementPropertyFormState.DeleteDialog?>(null)
    private val alertDialog = MutableStateFlow<MeasurementPropertyFormState.AlertDialog?>(null)

    override val state = com.faltenreich.diaguard.shared.architecture.combine(
        property,
        combine(property, decimalPlaces, mapValueRangeState::invoke),
        unitSuggestions,
        errorBar,
        deleteDialog,
        alertDialog,
        ::MeasurementPropertyFormState,
    )

    override suspend fun handleIntent(intent: MeasurementPropertyFormIntent) {
        when (intent) {
            is MeasurementPropertyFormIntent.UpdateProperty ->
                update(name = intent.name)
            is MeasurementPropertyFormIntent.UpdateAggregationStyle ->
                update(aggregationStyle = intent.aggregationStyle)
            is MeasurementPropertyFormIntent.UpdateValueRange ->
                update(valueRange = intent.valueRange)
            is MeasurementPropertyFormIntent.OpenUnitSearch ->
                pushScreen(MeasurementUnitListScreen(mode = MeasurementUnitListMode.FIND))
            is MeasurementPropertyFormIntent.SelectUnit ->
                update(intent.unit)
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
                delete(intent)
        }
    }

    private fun update(name: String) {
        property.update { property ->
            when (property) {
                is MeasurementProperty.Seed -> property.copy(name = name)
                is MeasurementProperty.User -> property.copy(name = name)
                is MeasurementProperty.Local -> property.copy(name = name)
            }
        }
        errorBar.update { null }
    }

    private fun update(aggregationStyle: MeasurementAggregationStyle) {
        property.update { property ->
            when (property) {
                is MeasurementProperty.Seed -> property.copy(aggregationStyle = aggregationStyle)
                is MeasurementProperty.User -> property.copy(aggregationStyle = aggregationStyle)
                is MeasurementProperty.Local -> property.copy(aggregationStyle = aggregationStyle)
            }
        }
    }

    private fun update(valueRange: MeasurementValueRangeState) {
        property.update { property ->
            val update = MeasurementValueRange(
                minimum = valueRange.minimum.toDoubleOrNull() ?: 0.0,
                low = valueRange.low.toDoubleOrNull(),
                target = valueRange.target.toDoubleOrNull(),
                high = valueRange.high.toDoubleOrNull(),
                maximum = valueRange.maximum.toDoubleOrNull() ?: Double.MAX_VALUE,
                isHighlighted = valueRange.isHighlighted,
            )
            when (property) {
                is MeasurementProperty.Seed -> property.copy(range = update)
                is MeasurementProperty.User -> property.copy(range = update)
                is MeasurementProperty.Local -> property.copy(range = update)
            }
        }
    }

    private fun update(unit: MeasurementUnit.Local) {
        property.update { property ->
            when (property) {
                is MeasurementProperty.Seed -> property.apply { this.unit = unit }
                is MeasurementProperty.User -> property.apply { this.unit = unit }
                is MeasurementProperty.Local -> property.copy(unit = unit)
            }
        }
        errorBar.update { null }
    }

    private suspend fun submit() {
        when (storeProperty(property.value)) {
            is Result.Success -> {
                storeCategory(category)
                popScreen()
            }
            is Result.Failure -> errorBar.update { MeasurementPropertyFormState.ErrorBar }
        }
    }

    private suspend fun delete(intent: MeasurementPropertyFormIntent.Delete) {
        when (val property = property.value) {
            is MeasurementProperty.Seed,
            is MeasurementProperty.User-> popScreen()
            is MeasurementProperty.Local -> {
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
    }
}