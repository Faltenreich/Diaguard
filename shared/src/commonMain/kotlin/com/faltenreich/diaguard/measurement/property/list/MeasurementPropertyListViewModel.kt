package com.faltenreich.diaguard.measurement.property.list

import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MeasurementPropertyListViewModel(
    getMeasurementProperties: GetMeasurementPropertiesUseCase = inject(),
) : ViewModel() {

    private val state = getMeasurementProperties()
    val viewState = state.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}