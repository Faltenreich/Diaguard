package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.category.form.CreateMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.form.UpdateMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.screen.MeasurementPropertyFormScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MeasurementPropertyListViewModel(
    private val updateMeasurementProperty: UpdateMeasurementPropertyUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val createMeasurementProperty: CreateMeasurementPropertyUseCase = inject(),
) : ViewModel<MeasurementPropertyListState, MeasurementPropertyListIntent, Unit>() {

    private val showFormDialog = MutableStateFlow(false)

    override val state = showFormDialog.map(::MeasurementPropertyListState)

    override fun handleIntent(intent: MeasurementPropertyListIntent) = with(intent) {
        when (this) {
            is MeasurementPropertyListIntent.DecrementSortIndex -> decrementSortIndex(property, inProperties)
            is MeasurementPropertyListIntent.IncrementSortIndex -> incrementSortIndex(property, inProperties)
            is MeasurementPropertyListIntent.EditProperty -> navigateToScreen(MeasurementPropertyFormScreen(property))
            is MeasurementPropertyListIntent.ShowFormDialog -> showFormDialog.value  = true
            is MeasurementPropertyListIntent.HideFormDialog -> showFormDialog.value = false
            is MeasurementPropertyListIntent.CreateProperty -> createMeasurementProperty(
                propertyKey = null,
                propertyName = propertyName,
                // TODO: Make user-customizable
                propertyRange = MeasurementValueRange(
                    minimum = 0.0,
                    low = null,
                    target = null,
                    high = null,
                    maximum = Double.MAX_VALUE,
                    isHighlighted = false,
                ),
                propertySortIndex = properties.maxOfOrNull(MeasurementProperty::sortIndex)?.plus(1) ?: 0,
                categoryId = categoryId,
                unitKey = null,
                unitName = unitName,
            )
        }
    }

    private fun decrementSortIndex(
        property: MeasurementProperty,
        inProperties: List<MeasurementProperty>,
    ) {
        swapSortIndexes(first = property, second = inProperties.last { it.sortIndex < property.sortIndex })
    }

    private fun incrementSortIndex(
        property: MeasurementProperty,
        inProperties: List<MeasurementProperty>,
    ) {
        swapSortIndexes(first = property, second = inProperties.first { it.sortIndex > property.sortIndex })
    }

    private fun swapSortIndexes(
        first: MeasurementProperty,
        second: MeasurementProperty,
    ) {
        updateMeasurementProperty(first.copy(sortIndex = second.sortIndex))
        updateMeasurementProperty(second.copy(sortIndex = first.sortIndex))
    }
}