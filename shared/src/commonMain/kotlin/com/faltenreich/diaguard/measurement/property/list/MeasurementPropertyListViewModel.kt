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

    fun decrementSortIndex(property: MeasurementProperty) {
        setSortIndex(property, sortIndex = property.sortIndex - 1)
    }

    fun incrementSortIndex(property: MeasurementProperty) {
        setSortIndex(property, sortIndex = property.sortIndex + 1)
    }

    private fun setSortIndex(property: MeasurementProperty, sortIndex: Long) {
        val properties = (viewState.value as? MeasurementPropertyListViewState.Loaded)?.listItems ?: return
        val isDecrementing = sortIndex < property.sortIndex

        setMeasurementPropertySortIndex(property, sortIndex = sortIndex)

        val replacement = properties.first { it.sortIndex == sortIndex }
        val replacementSortIndex = if (isDecrementing) sortIndex + 1 else sortIndex -1
        setMeasurementPropertySortIndex(replacement, sortIndex = replacementSortIndex)
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
            sortIndex = properties.maxOf(MeasurementProperty::sortIndex) + 1
        )
    }
}