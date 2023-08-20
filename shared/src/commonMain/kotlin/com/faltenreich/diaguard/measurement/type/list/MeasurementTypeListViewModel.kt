package com.faltenreich.diaguard.measurement.type.list

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MeasurementTypeListViewModel(
    property: MeasurementProperty,
    getMeasurementTypesUseCase: GetMeasurementTypesUseCase = inject(),
    private val setMeasurementTypeSortIndex: SetMeasurementTypeSortIndexUseCase = inject(),
) : ViewModel() {

    private val state = getMeasurementTypesUseCase(property).map { types ->
        MeasurementTypeListViewState.Loaded(property = property, listItems = types)
    }
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MeasurementTypeListViewState.Loading(property),
    )

    fun decrementSortIndex(type: MeasurementType) {
        setSortIndex(type, sortIndex = type.sortIndex - 1)
    }

    fun incrementSortIndex(type: MeasurementType) {
        setSortIndex(type, sortIndex = type.sortIndex + 1)
    }

    private fun setSortIndex(type: MeasurementType, sortIndex: Long) {
        val types = (viewState.value as? MeasurementTypeListViewState.Loaded)?.listItems ?: return
        val isDecrementing = sortIndex < type.sortIndex

        setMeasurementTypeSortIndex(type = type, sortIndex = sortIndex)

        val replacement = types.first { it.sortIndex == sortIndex }
        val replacementSortIndex = if (isDecrementing) sortIndex + 1 else sortIndex -1
        setMeasurementTypeSortIndex(type = replacement, sortIndex = replacementSortIndex)
    }
}