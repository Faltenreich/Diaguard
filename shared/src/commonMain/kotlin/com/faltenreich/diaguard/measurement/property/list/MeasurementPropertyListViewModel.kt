package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.form.UpdateMeasurementPropertyUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MeasurementPropertyListViewModel(
    private val dispatcher: CoroutineDispatcher = inject(),
    getMeasurementProperties: GetMeasurementPropertiesUseCase = inject(),
    private val updateMeasurementProperty: UpdateMeasurementPropertyUseCase = inject(),
    private val createMeasurementProperty: CreateMeasurementPropertyUseCase = inject(),
) : ViewModel {

    private val showFormDialog = MutableStateFlow(false)

    private val state = combine(
        showFormDialog,
        getMeasurementProperties(),
        MeasurementPropertyListViewState::Loaded,
    ).flowOn(dispatcher)
    val viewState = state.stateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = MeasurementPropertyListViewState.Loading(showFormDialog = false),
    )

    private val properties: List<MeasurementProperty>?
        get() = (viewState.value as? MeasurementPropertyListViewState.Loaded)?.listItems

    fun decrementSortIndex(property: MeasurementProperty) = scope.launch(dispatcher) {
        val properties = properties ?: return@launch
        swapSortIndexes(first = property, second = properties.last { it.sortIndex < property.sortIndex })
    }

    fun incrementSortIndex(property: MeasurementProperty) = scope.launch(dispatcher) {
        val properties = properties ?: return@launch
        swapSortIndexes(first = property, second = properties.first { it.sortIndex > property.sortIndex })
    }

    private fun swapSortIndexes(
        first: MeasurementProperty,
        second: MeasurementProperty,
    ) = scope.launch(dispatcher) {
        updateMeasurementProperty(first.copy(sortIndex = second.sortIndex))
        updateMeasurementProperty(second.copy(sortIndex = first.sortIndex))
    }

    fun showFormDialog() = scope.launch(dispatcher) {
        showFormDialog.value = true
    }

    fun hideFormDialog() = scope.launch(dispatcher) {
        showFormDialog.value = false
    }

    fun createProperty(name: String) = scope.launch(dispatcher) {
        val properties = (viewState.value as? MeasurementPropertyListViewState.Loaded)?.listItems ?: return@launch
        createMeasurementProperty(
            name = name,
            key = null,
            icon = null,
            sortIndex = properties.maxOf(MeasurementProperty::sortIndex) + 1,
        )
    }
}