package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormModal
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormScreen
import com.faltenreich.diaguard.measurement.property.form.UpdateMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.StoreMeasurementUnitUseCase
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.navigation.bar.snackbar.ShowSnackbarUseCase
import com.faltenreich.diaguard.navigation.modal.CloseModalUseCase
import com.faltenreich.diaguard.navigation.modal.OpenModalUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.error_unknown
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class MeasurementPropertyListViewModel(
    private val createProperty: CreateMeasurementPropertyUseCase,
    private val storeUnit: StoreMeasurementUnitUseCase,
    private val updateProperty: UpdateMeasurementPropertyUseCase,
    private val pushScreen: PushScreenUseCase,
    private val openModal: OpenModalUseCase,
    private val closeModal: CloseModalUseCase,
    private val showSnackbar: ShowSnackbarUseCase,
) : ViewModel<Unit, MeasurementPropertyListIntent, Unit>() {

    override val state = flowOf(Unit)

    override suspend fun handleIntent(intent: MeasurementPropertyListIntent) = with(intent) {
        when (this) {
            is MeasurementPropertyListIntent.DecrementSortIndex -> decrementSortIndex(property, inProperties)
            is MeasurementPropertyListIntent.IncrementSortIndex -> incrementSortIndex(property, inProperties)
            is MeasurementPropertyListIntent.EditProperty -> editProperty(property)
            is MeasurementPropertyListIntent.CreateProperty -> createProperty(category, properties)
        }
    }

    private suspend fun decrementSortIndex(
        property: MeasurementProperty.Local,
        inProperties: List<MeasurementProperty.Local>,
    ) {
        val previous = inProperties.lastOrNull { it.sortIndex < property.sortIndex } ?: run {
            showSnackbar(Res.string.error_unknown)
            return
        }
        swapSortIndexes(first = property, second = previous)
    }

    private suspend fun incrementSortIndex(
        property: MeasurementProperty.Local,
        inProperties: List<MeasurementProperty.Local>,
    ) {
        val next = inProperties.firstOrNull { it.sortIndex > property.sortIndex } ?: run {
            showSnackbar(Res.string.error_unknown)
            return
        }
        swapSortIndexes(first = property, second = next)
    }

    private fun swapSortIndexes(
        first: MeasurementProperty.Local,
        second: MeasurementProperty.Local,
    ) {
        updateProperty(first.copy(sortIndex = second.sortIndex))
        updateProperty(second.copy(sortIndex = first.sortIndex))
    }

    private suspend fun editProperty(property: MeasurementProperty.Local) {
        pushScreen(MeasurementPropertyFormScreen(property))
    }

    private suspend fun createProperty(
        category: MeasurementCategory.Local,
        properties: List<MeasurementProperty.Local>,
    ) {
        openModal(
            MeasurementPropertyFormModal(
                onDismissRequest = { scope.launch { closeModal() } },
                onConfirmRequest = { propertyName, unitName ->
                    val unit = storeUnit(
                        MeasurementUnit.User(
                            name = unitName,
                            // TODO: Make user-customizable
                            abbreviation = unitName,
                        )
                    )
                    val property = createProperty(
                        name = propertyName,
                        sortIndex = properties.maxOfOrNull(MeasurementProperty::sortIndex)
                            ?.plus(1) ?: 0,
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
                        unit = unit,
                    )
                    scope.launch {
                        closeModal()
                        pushScreen (MeasurementPropertyFormScreen(property))
                    }
                }
            )
        )
    }
}