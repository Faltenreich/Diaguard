package com.faltenreich.diaguard.measurement.unit.list

import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import kotlinx.coroutines.flow.emptyFlow

class MeasurementUnitSelectionViewModel : ViewModel<Unit, Unit, MeasurementUnitSelectionEvent>() {

    override val state = emptyFlow<Unit>()
}