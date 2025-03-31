package com.faltenreich.diaguard.measurement.unit.list

import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.map

class MeasurementUnitListViewModel(
    getUnits: GetMeasurementUnitsUseCase,
) : ViewModel<MeasurementUnitListState, MeasurementUnitListIntent, Unit>() {

    override val state = getUnits().map(::MeasurementUnitListState)

    override suspend fun handleIntent(intent: MeasurementUnitListIntent) {
        when (intent) {
            is MeasurementUnitListIntent.Create -> TODO()
        }
    }
}