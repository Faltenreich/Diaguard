package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MeasurementPropertyListViewModel(
    getMeasurementProperties: GetMeasurementPropertiesUseCase = inject(),
    private val setMeasurementPropertySortIndex: SetMeasurementPropertySortIndexUseCase = inject(),
    private val createMeasurementProperty: CreateMeasurementPropertyUseCase = inject(),
) : ViewModel() {

    private val showFormDialog = MutableStateFlow(false)

    private val state = combine(
        showFormDialog,
        getMeasurementProperties(),
    ) { showFormDialog, properties ->
        MeasurementPropertyListViewState.Loaded(showFormDialog, properties)
    }
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MeasurementPropertyListViewState.Loading(showFormDialog = false),
    )

    private val properties = (viewState.value as? MeasurementPropertyListViewState.Loaded)?.listItems

    fun decrementSortIndex(property: MeasurementProperty) {
        val properties = properties ?: return
        swapSortIndexes(first = property, second = properties.last { it.sortIndex < property.sortIndex })
    }

    fun incrementSortIndex(property: MeasurementProperty) {
        val properties = properties ?: return
        swapSortIndexes(first = property, second = properties.first { it.sortIndex > property.sortIndex })
    }

    private fun swapSortIndexes(
        first: MeasurementProperty,
        second: MeasurementProperty,
    ) {
        val firstSortIndex = first.sortIndex
        val secondSortIndex = second.sortIndex
        setMeasurementPropertySortIndex(first, sortIndex = secondSortIndex)
        setMeasurementPropertySortIndex(second, sortIndex = firstSortIndex)
    }

    fun showFormDialog() {
        showFormDialog.value = true
    }

    fun hideFormDialog() {
        showFormDialog.value = false
    }

    fun createProperty(name: String) {
        val properties = (viewState.value as? MeasurementPropertyListViewState.Loaded)?.listItems ?: return
        createMeasurementProperty(
            name = name,
            icon = null,
            sortIndex = properties.maxOf(MeasurementProperty::sortIndex) + 1,
        )
    }
}