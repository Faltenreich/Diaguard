package com.faltenreich.diaguard.measurement.type.list

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.form.UpdateMeasurementTypeUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class MeasurementTypeListViewModel(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val updateMeasurementType: UpdateMeasurementTypeUseCase = inject(),
) : ViewModel() {

    fun decrementSortIndex(
        type: MeasurementType,
        inTypes: List<MeasurementType>,
    ) = viewModelScope.launch(dispatcher) {
        swapSortIndexes(first = type, second = inTypes.last { it.sortIndex < type.sortIndex })
    }

    fun incrementSortIndex(
        type: MeasurementType,
        inTypes: List<MeasurementType>,
    ) = viewModelScope.launch(dispatcher) {
        swapSortIndexes(first = type, second = inTypes.first { it.sortIndex > type.sortIndex })
    }

    private fun swapSortIndexes(
        first: MeasurementType,
        second: MeasurementType,
    ) = viewModelScope.launch(dispatcher) {
        updateMeasurementType(first.copy(sortIndex = second.sortIndex))
        updateMeasurementType(second.copy(sortIndex = first.sortIndex))
    }
}