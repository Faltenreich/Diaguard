package com.faltenreich.diaguard.measurement.unit.list

import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.StoreMeasurementUnitUseCase
import com.faltenreich.diaguard.measurement.unit.ValidateMeasurementUnitUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.validation.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

class MeasurementUnitListViewModel(
    getUnits: GetMeasurementUnitsUseCase,
    private val validateUnit: ValidateMeasurementUnitUseCase,
    private val storeUnit: StoreMeasurementUnitUseCase,
) : ViewModel<MeasurementUnitListState, MeasurementUnitListIntent, Unit>() {

    private val formDialog = MutableStateFlow<MeasurementUnitListState.FormDialog?>(null)

    override val state = combine(
        getUnits(),
        formDialog,
        ::MeasurementUnitListState,
    )

    override suspend fun handleIntent(intent: MeasurementUnitListIntent) {
        when (intent) {
            is MeasurementUnitListIntent.OpenFormDialog ->
                formDialog.update { MeasurementUnitListState.FormDialog(intent.unit) }
            is MeasurementUnitListIntent.CloseFormDialog ->
                formDialog.update { null }
            is MeasurementUnitListIntent.StoreUnit ->
                store(intent)
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
        when (val result = validateUnit(unit)) {
            is ValidationResult.Success -> {
                formDialog.update { null }
                storeUnit(unit)
            }
            is ValidationResult.Failure -> {
                formDialog.update { MeasurementUnitListState.FormDialog(error = result.error) }
            }
        }
    }
}