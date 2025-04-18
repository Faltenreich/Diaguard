package com.faltenreich.diaguard.measurement.unit.list

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.StoreMeasurementUnitUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

class MeasurementUnitListViewModel(
    getUnits: GetMeasurementUnitsUseCase,
    private val storeUnit: StoreMeasurementUnitUseCase,
) : ViewModel<MeasurementUnitListState, MeasurementUnitListIntent, Unit>() {

    private val form = MutableStateFlow<MeasurementUnitListState.Form>(MeasurementUnitListState.Form.Hidden)

    override val state = combine(
        getUnits(),
        form,
        ::MeasurementUnitListState,
    )

    override suspend fun handleIntent(intent: MeasurementUnitListIntent) = with(intent) {
        when (this) {
            is MeasurementUnitListIntent.OpenFormDialog -> form.update { MeasurementUnitListState.Form.Shown(unit) }
            is MeasurementUnitListIntent.CloseFormDialog -> form.update { MeasurementUnitListState.Form.Hidden }
            is MeasurementUnitListIntent.StoreUnit -> store(this)
        }
    }

    private fun store(intent: MeasurementUnitListIntent.StoreUnit) = with(intent) {
        val unit = unit?.copy(
            name = name,
            abbreviation = abbreviation,
        ) ?: MeasurementUnit.User(
            name = name,
            abbreviation = abbreviation
        )
        storeUnit(unit)
        form.update { MeasurementUnitListState.Form.Hidden }
    }
}