package com.faltenreich.diaguard.measurement.unit.list

import com.faltenreich.diaguard.measurement.unit.form.MeasurementUnitFormModal
import com.faltenreich.diaguard.navigation.modal.OpenModalUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.map

class MeasurementUnitListViewModel(
    getUnits: GetMeasurementUnitsUseCase,
    private val openModal: OpenModalUseCase,
) : ViewModel<MeasurementUnitListState, MeasurementUnitListIntent, Unit>() {

    override val state = getUnits().map(::MeasurementUnitListState)

    override suspend fun handleIntent(intent: MeasurementUnitListIntent) = with(intent) {
        when (this) {
            is MeasurementUnitListIntent.Create -> openModal(MeasurementUnitFormModal())
            is MeasurementUnitListIntent.Edit -> openModal(MeasurementUnitFormModal(unit.id))
        }
    }
}