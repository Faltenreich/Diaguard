package com.faltenreich.diaguard.measurement.unit.list

import com.faltenreich.diaguard.measurement.type.form.UpdateMeasurementTypeUseCase
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class MeasurementUnitListViewModel(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val updateMeasurementType: UpdateMeasurementTypeUseCase = inject(),
) : ViewModel() {

    fun setSelectedUnit(unit: MeasurementUnit) = viewModelScope.launch(dispatcher) {
        updateMeasurementType(unit.type.copy(selectedUnitId = unit.id))
    }
}