package com.faltenreich.diaguard.measurement.type.list

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.form.UpdateMeasurementTypeUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MeasurementTypeListViewModel(
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
    ) {
        swapSortIndexes(first = type, second = inTypes.last { it.sortIndex < type.sortIndex })
    }

    private fun incrementSortIndex(
        type: MeasurementType,
        inTypes: List<MeasurementType>,
    ) {
        swapSortIndexes(first = type, second = inTypes.first { it.sortIndex > type.sortIndex })
    }

    private fun swapSortIndexes(
        first: MeasurementType,
        second: MeasurementType,
    ) {
        updateMeasurementType(first.copy(sortIndex = second.sortIndex))
        updateMeasurementType(second.copy(sortIndex = first.sortIndex))
    }
}