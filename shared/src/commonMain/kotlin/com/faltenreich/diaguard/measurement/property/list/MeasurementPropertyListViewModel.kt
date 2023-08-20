package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MeasurementPropertyListViewModel(
    getMeasurementProperties: GetMeasurementPropertiesUseCase = inject(),
    private val setMeasurementPropertySortIndex: SetMeasurementPropertySortIndexUseCase = inject(),
) : ViewModel() {

    private val state = getMeasurementProperties().map { properties ->
        MeasurementPropertyListViewState.Loaded(properties)
    }
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MeasurementPropertyListViewState.Loading,
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

        setMeasurementPropertySortIndex(property = property, sortIndex = sortIndex)

        val replacement = properties.first { it.sortIndex == sortIndex }
        val replacementSortIndex = if (isDecrementing) sortIndex + 1 else sortIndex -1
        setMeasurementPropertySortIndex(property = replacement, sortIndex = replacementSortIndex)
    }
}