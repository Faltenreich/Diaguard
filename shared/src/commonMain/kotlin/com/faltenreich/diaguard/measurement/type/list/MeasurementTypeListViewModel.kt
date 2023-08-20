package com.faltenreich.diaguard.measurement.type.list

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MeasurementTypeListViewModel(
    property: MeasurementProperty,
    getMeasurementTypesUseCase: GetMeasurementTypesUseCase = inject(),
) : ViewModel() {

    private val state = getMeasurementTypesUseCase(property).map { types ->
        MeasurementTypeListViewState.Loaded(types)
    }
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MeasurementTypeListViewState.Loading,
    )
}