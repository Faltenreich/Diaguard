package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.form.UpdateMeasurementPropertyUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.screen.MeasurementPropertyFormScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class MeasurementPropertyListViewModel(
    getMeasurementProperties: GetMeasurementPropertiesUseCase = inject(),
    private val updateMeasurementProperty: UpdateMeasurementPropertyUseCase = inject(),
    private val createMeasurementProperty: CreateMeasurementPropertyUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
) : ViewModel<MeasurementPropertyListViewState, MeasurementPropertyListIntent>() {

    private val showFormDialog = MutableStateFlow(false)

    override val state = combine(
        showFormDialog,
        getMeasurementProperties(),
        MeasurementPropertyListViewState::Loaded,
    )

    private val properties: List<MeasurementProperty>?
        get() = (stateInScope.value as? MeasurementPropertyListViewState.Loaded)?.listItems

    override fun onIntent(intent: MeasurementPropertyListIntent) {
        when (intent) {
            is MeasurementPropertyListIntent.ShowFormDialog -> showFormDialog.value = true
            is MeasurementPropertyListIntent.HideFormDialog -> showFormDialog.value = false
            is MeasurementPropertyListIntent.DecrementSortIndex -> decrementSortIndex(intent.property)
            is MeasurementPropertyListIntent.IncrementSortIndex -> incrementSortIndex(intent.property)
            is MeasurementPropertyListIntent.CreateProperty -> createMeasurementProperty(
                name = intent.name,
                key = null,
                icon = null,
                sortIndex = intent.other.maxOf(MeasurementProperty::sortIndex) + 1,
            )
            is MeasurementPropertyListIntent.EditProperty -> navigateToScreen(
                MeasurementPropertyFormScreen(intent.property)
            )
        }
    }

    private fun decrementSortIndex(property: MeasurementProperty) {
        val properties = properties ?: return
        swapSortIndexes(first = property, second = properties.last { it.sortIndex < property.sortIndex })
    }

    private fun incrementSortIndex(property: MeasurementProperty) {
        val properties = properties ?: return
        swapSortIndexes(first = property, second = properties.first { it.sortIndex > property.sortIndex })
    }

    private fun swapSortIndexes(
        first: MeasurementProperty,
        second: MeasurementProperty,
    ) {
        updateMeasurementProperty(first.copy(sortIndex = second.sortIndex))
        updateMeasurementProperty(second.copy(sortIndex = first.sortIndex))
    }
}