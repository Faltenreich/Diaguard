package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MeasurementPropertyFormViewModel(
    property: MeasurementProperty,
    getMeasurementTypesUseCase: GetMeasurementTypesUseCase = inject(),
    private val setMeasurementTypeSortIndex: SetMeasurementTypeSortIndexUseCase = inject(),
) : ViewModel() {

    var name by mutableStateOf<String>(property.name)
    var icon by mutableStateOf<String>(property.icon ?: "")

    private val state = getMeasurementTypesUseCase(property).map { types ->
        MeasurementPropertyFormViewState.Loaded(property, types)
    }
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MeasurementPropertyFormViewState.Loading(property),
    )

    fun decrementSortIndex(type: MeasurementType) {
        setSortIndex(type, sortIndex = type.sortIndex - 1)
    }

    fun incrementSortIndex(type: MeasurementType) {
        setSortIndex(type, sortIndex = type.sortIndex + 1)
    }

    private fun setSortIndex(type: MeasurementType, sortIndex: Long) {
        val types = (viewState.value as? MeasurementPropertyFormViewState.Loaded)?.types ?: return
        val isDecrementing = sortIndex < type.sortIndex

        setMeasurementTypeSortIndex(type = type, sortIndex = sortIndex)

        val replacement = types.first { it.sortIndex == sortIndex }
        val replacementSortIndex = if (isDecrementing) sortIndex + 1 else sortIndex -1
        setMeasurementTypeSortIndex(type = replacement, sortIndex = replacementSortIndex)
    }
}