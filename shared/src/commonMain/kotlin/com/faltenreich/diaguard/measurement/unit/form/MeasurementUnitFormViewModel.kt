package com.faltenreich.diaguard.measurement.unit.form

import com.faltenreich.diaguard.navigation.modal.CloseModalUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.emptyFlow

class MeasurementUnitFormViewModel(
    private val closeModal: CloseModalUseCase,
) : ViewModel<Unit, MeasurementUnitFormIntent, Unit>() {

    override val state = emptyFlow<Unit>()

    override suspend fun handleIntent(intent: MeasurementUnitFormIntent) {
        when (intent) {
            is MeasurementUnitFormIntent.Close -> closeModal()
            is MeasurementUnitFormIntent.Submit -> TODO()
        }
    }
}