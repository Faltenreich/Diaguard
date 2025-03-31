package com.faltenreich.diaguard.measurement.unit.list

import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.map

class MeasurementUnitListViewModel(
    getUnits: GetMeasurementUnitsUseCase,
) : ViewModel<MeasurementUnitListState, Unit, Unit>() {

    override val state = getUnits().map(::MeasurementUnitListState)
}