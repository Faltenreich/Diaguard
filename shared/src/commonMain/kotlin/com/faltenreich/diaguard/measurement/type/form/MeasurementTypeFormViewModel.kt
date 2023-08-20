package com.faltenreich.diaguard.measurement.type.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MeasurementTypeFormViewModel(
    type: MeasurementType,
) : ViewModel() {

    var name by mutableStateOf<String>(type.name)
    var selectedUnit by mutableStateOf<MeasurementUnit>(type.selectedUnit)

    private val state = flowOf(type).map { type -> MeasurementTypeFormViewState.Loaded(type) }
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MeasurementTypeFormViewState.Loading(type)
    )
}