package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.form.CreateMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.property.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormModal
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormScreen
import com.faltenreich.diaguard.measurement.property.form.UpdateMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class MeasurementPropertyListViewModel(
    private val createProperty: CreateMeasurementPropertyUseCase,
    private val updateProperty: UpdateMeasurementPropertyUseCase,
    private val navigateToScreen: NavigateToScreenUseCase,
    private val openModal: OpenModalUseCase,
    private val closeModal: CloseModalUseCase,
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

    private fun decrementSortIndex(
        property: MeasurementProperty.Local,
        inProperties: List<MeasurementProperty.Local>,
    ) {
        swapSortIndexes(first = property, second = inProperties.last { it.sortIndex < property.sortIndex })
    }

    private fun incrementSortIndex(
        property: MeasurementProperty.Local,
        inProperties: List<MeasurementProperty.Local>,
    ) {
        swapSortIndexes(first = property, second = inProperties.first { it.sortIndex > property.sortIndex })
    }

    private fun swapSortIndexes(
        first: MeasurementProperty.Local,
        second: MeasurementProperty.Local,
    ) {
        updateProperty(first.copy(sortIndex = second.sortIndex))
        updateProperty(second.copy(sortIndex = first.sortIndex))
    }

    private suspend fun editProperty(property: MeasurementProperty.Local) {
        navigateToScreen(MeasurementPropertyFormScreen(property))
    }

    private fun createProperty(
        category: MeasurementCategory.Local,
        properties: List<MeasurementProperty.Local>,
    ) {
        openModal(
            MeasurementPropertyFormModal(
                onDismissRequest = closeModal::invoke,
                onConfirmRequest = { propertyName, unitName ->
                    val property = createProperty(
                        propertyName = propertyName,
                        propertySortIndex = properties.maxOfOrNull(MeasurementProperty::sortIndex)
                            ?.plus(1) ?: 0,
                        // TODO: Make user-customizable
                        propertyAggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
                        // TODO: Make user-customizable
                        propertyRange = MeasurementValueRange(
                            minimum = 0.0,
                            low = null,
                            target = null,
                            high = null,
                            maximum = 10_000.0,
                            isHighlighted = false,
                        ),
                        category = category,
                        unitName = unitName,
                        unitIsSelected = true,
                    )
                    closeModal()
                    scope.launch {
                        navigateToScreen (MeasurementPropertyFormScreen(property))
                    }
                }
            )
        )
    }
}