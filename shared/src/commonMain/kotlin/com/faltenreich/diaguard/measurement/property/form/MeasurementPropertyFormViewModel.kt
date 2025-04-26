package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.category.StoreMeasurementCategoryUseCase
import com.faltenreich.diaguard.measurement.category.form.GetMeasurementCategoryByIdUseCase
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.StoreMeasurementPropertyUseCase
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
    private val storeProperty: StoreMeasurementPropertyUseCase = inject(),
    private val deleteProperty: DeleteMeasurementPropertyUseCase = inject(),
    private val storeCategory: StoreMeasurementCategoryUseCase = inject(),
    private val pushScreen: PushScreenUseCase = inject(),
    private val popScreen: PopScreenUseCase = inject(),
    private val localization: Localization = inject(),
    private val numberFormatter: NumberFormatter = inject(),
) : ViewModel<MeasurementPropertyFormState, MeasurementPropertyFormIntent, Unit>() {

    private val propertyLocal = propertyId?.let(getPropertyById::invoke)
    // TODO: Extract into use case
    private val createUserProperty = {
        MeasurementProperty.User(
            name = "",
            sortIndex = getMaximumSortIndex(categoryId)?.plus(1) ?: 0,
            // TODO: Make user-customizable
            aggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
            // TODO: Make user-customizable
            range = MeasurementValueRange(
                minimum = 0.0,
                low = null,
                target = null,
                high = null,
                maximum = 10_000.0,
                isHighlighted = false,
            ),
            category = category,
        )
    }

    private val category = checkNotNull(getCategoryById(categoryId))
    private val property = MutableStateFlow(propertyLocal ?: createUserProperty())
    // TODO: Merge into property
    private val unit = MutableStateFlow((property.value as? MeasurementProperty.Local)?.unit)
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
            unit = unit?.name,
        )
    }

    private val unitSuggestions = propertyLocal?.let { property ->
        combine(
            unit,
            getUnitSuggestions(property),
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
                    isSelected = unit?.id == unitSuggestion.unit.id,
                )
            }
        }
    } ?: flowOf(emptyList())

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
                update(name = intent.name)
            is MeasurementPropertyFormIntent.UpdateValueRange ->
                update(valueRange = intent.valueRange)
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
                delete(intent)
        }
    }

    // TODO: Avoid redundant copy(), e.g. via interface method
    private fun update(name: String) {
        property.update { property ->
            when (property) {
                is MeasurementProperty.Seed -> property.copy(name = name)
                is MeasurementProperty.User -> property.copy(name = name)
                is MeasurementProperty.Local -> property.copy(name = name)
            }
        }
    }

    // TODO: Avoid redundant copy(), e.g. via interface method
    private fun update(valueRange: MeasurementValueRangeState) {
        property.update { property ->
            when (property) {
                is MeasurementProperty.Seed -> property.copy(
                    range = MeasurementValueRange(
                        minimum = valueRange.minimum.toDoubleOrNull() ?: 0.0,
                        low = valueRange.low.toDoubleOrNull(),
                        target = valueRange.target.toDoubleOrNull(),
                        high = valueRange.high.toDoubleOrNull(),
                        maximum = valueRange.maximum.toDoubleOrNull() ?: Double.MAX_VALUE,
                        isHighlighted = valueRange.isHighlighted,
                    ),
                )
                is MeasurementProperty.User -> property.copy(
                    range = MeasurementValueRange(
                        minimum = valueRange.minimum.toDoubleOrNull() ?: 0.0,
                        low = valueRange.low.toDoubleOrNull(),
                        target = valueRange.target.toDoubleOrNull(),
                        high = valueRange.high.toDoubleOrNull(),
                        maximum = valueRange.maximum.toDoubleOrNull() ?: Double.MAX_VALUE,
                        isHighlighted = valueRange.isHighlighted,
                    ),
                )
                is MeasurementProperty.Local -> property.copy(
                    range = MeasurementValueRange(
                        minimum = valueRange.minimum.toDoubleOrNull() ?: 0.0,
                        low = valueRange.low.toDoubleOrNull(),
                        target = valueRange.target.toDoubleOrNull(),
                        high = valueRange.high.toDoubleOrNull(),
                        maximum = valueRange.maximum.toDoubleOrNull() ?: Double.MAX_VALUE,
                        isHighlighted = valueRange.isHighlighted,
                    ),
                )
            }
        }
    }

    private suspend fun submit() {
        // TODO: Validate
        val unit = unit.value ?: return
        val property = property.value.also { property ->
            if (property is MeasurementProperty.User) {
                property.unit = unit
            }
        }
        storeProperty(property)
        storeCategory(category)
        popScreen()
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