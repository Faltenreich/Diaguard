package com.faltenreich.diaguard.measurement.type.list

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.form.UpdateMeasurementTypeUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class MeasurementTypeListViewModel(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val updateMeasurementType: UpdateMeasurementTypeUseCase = inject(),
) : ViewModel<Unit, MeasurementTypeListIntent>() {

    override val state: Flow<Unit>
        get() = flowOf(Unit)

    override fun onIntent(intent: MeasurementTypeListIntent) {
        when (intent) {
            is MeasurementTypeListIntent.DecrementSortIndex -> decrementSortIndex(intent.type, intent.inTypes)
            is MeasurementTypeListIntent.IncrementSortIndex -> incrementSortIndex(intent.type, intent.inTypes)
        }
    }

    private fun decrementSortIndex(
        type: MeasurementType,
        inTypes: List<MeasurementType>,
    ) = scope.launch(dispatcher) {
        swapSortIndexes(first = type, second = inTypes.last { it.sortIndex < type.sortIndex })
    }

    private fun incrementSortIndex(
        type: MeasurementType,
        inTypes: List<MeasurementType>,
    ) = scope.launch(dispatcher) {
        swapSortIndexes(first = type, second = inTypes.first { it.sortIndex > type.sortIndex })
    }

    private fun swapSortIndexes(
        first: MeasurementType,
        second: MeasurementType,
    ) = scope.launch(dispatcher) {
        updateMeasurementType(first.copy(sortIndex = second.sortIndex))
        updateMeasurementType(second.copy(sortIndex = first.sortIndex))
    }
}