package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.category.usecase.StoreMeasurementCategoryUseCase
import com.faltenreich.diaguard.measurement.category.usecase.GetMeasurementCategoryByIdUseCase
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.aggregationstyle.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.range.MeasurementValueRange
import com.faltenreich.diaguard.measurement.property.usecase.DeleteMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.property.usecase.GetMaximumSortIndexOfMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.property.usecase.GetMeasurementPropertyBdIdUseCase
import com.faltenreich.diaguard.measurement.property.usecase.StoreMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListMode
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListScreen
import com.faltenreich.diaguard.measurement.unit.usecase.GetMeasurementUnitSuggestionsUseCase
import com.faltenreich.diaguard.navigation.screen.PopScreenUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.architecture.ViewModel
import com.faltenreich.diaguard.injection.inject
import com.faltenreich.diaguard.shared.result.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update

class MeasurementPropertyFormViewModel(
    categoryId: Long,
    propertyId: Long?,
    getCategoryById: GetMeasurementCategoryByIdUseCase = inject(),
    getPropertyById: GetMeasurementPropertyBdIdUseCase = inject(),
    getMaximumSortIndexOfProperty: GetMaximumSortIndexOfMeasurementPropertyUseCase = inject(),
    getUnitSuggestions: GetMeasurementUnitSuggestionsUseCase = inject(),
    getPreference: GetPreferenceUseCase = inject(),
    private val createState: MeasurementPropertyFormStateFactory = inject(),
    private val storeProperty: StoreMeasurementPropertyUseCase = inject(),
    private val deleteProperty: DeleteMeasurementPropertyUseCase = inject(),
    private val storeCategory: StoreMeasurementCategoryUseCase = inject(),
    private val pushScreen: PushScreenUseCase = inject(),
    private val popScreen: PopScreenUseCase = inject(),
) : ViewModel<MeasurementPropertyFormState, MeasurementPropertyFormIntent, Unit>() {

    private val category = checkNotNull(getCategoryById(categoryId))
    private val propertyLocal = propertyId?.let(getPropertyById::invoke)
    private val property = MutableStateFlow(
        propertyLocal
            ?: MeasurementProperty.User(
                sortIndex = getMaximumSortIndexOfProperty(categoryId)?.plus(1) ?: 0,
                category = category,
            )
    )

    private val errorBar = MutableStateFlow<MeasurementPropertyFormState.ErrorBar?>(null)
    private val dialog = MutableStateFlow<MeasurementPropertyFormState.Dialog?>(null)

    override val state = combine(
        property,
        propertyLocal?.let(getUnitSuggestions::invoke) ?: flowOf(emptyList()),
        getPreference(DecimalPlacesPreference),
        errorBar,
        dialog,
        createState::invoke,
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
            is MeasurementPropertyFormIntent.OpenDialog ->
                dialog.update { intent.dialog }
            is MeasurementPropertyFormIntent.CloseDialog ->
                dialog.update { null }
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

    private fun update(valueRange: MeasurementPropertyFormState.ValueRange) {
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
                        dialog.update { MeasurementPropertyFormState.Dialog.Delete }
                    } else {
                        deleteProperty(property)
                        popScreen()
                    }
                } else {
                    dialog.update { MeasurementPropertyFormState.Dialog.Alert }
                }
            }
        }
    }
}