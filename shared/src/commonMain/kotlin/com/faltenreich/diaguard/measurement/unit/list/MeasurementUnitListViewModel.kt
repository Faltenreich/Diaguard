package com.faltenreich.diaguard.measurement.unit.list

import com.faltenreich.diaguard.measurement.type.form.UpdateMeasurementTypeUseCase
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class MeasurementUnitListViewModel(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val updateMeasurementType: UpdateMeasurementTypeUseCase = inject(),
) : ViewModel<Unit>() {

    override val state: Flow<Unit>
        get() = flowOf(Unit)

    fun setSelectedUnit(unit: MeasurementUnit) = scope.launch(dispatcher) {
        updateMeasurementType(unit.type.copy(selectedUnitId = unit.id))
    }
}