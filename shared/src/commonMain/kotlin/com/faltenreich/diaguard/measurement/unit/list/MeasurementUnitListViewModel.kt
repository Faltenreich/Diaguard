package com.faltenreich.diaguard.measurement.unit.list

import com.faltenreich.diaguard.architecture.either.ValidationResult
import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.injection.inject
import com.faltenreich.diaguard.measurement.unit.usecase.GetMeasurementUnitsUseCase
import com.faltenreich.diaguard.measurement.unit.usecase.StoreMeasurementUnitUseCase
import com.faltenreich.diaguard.measurement.unit.usecase.ValidateMeasurementUnitUseCase
import com.faltenreich.diaguard.navigation.screen.PopScreenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

class MeasurementUnitListViewModel(
    val mode: MeasurementUnitListMode,
    getUnits: GetMeasurementUnitsUseCase = inject(),
    private val validateUnit: ValidateMeasurementUnitUseCase = inject(),
    private val storeUnit: StoreMeasurementUnitUseCase = inject(),
    private val popScreen: PopScreenUseCase = inject(),
) : ViewModel<MeasurementUnitListState, MeasurementUnitListIntent, Unit>() {

    private val formDialog = MutableStateFlow<MeasurementUnitListState.FormDialog?>(null)

    override val state = combine(
        getUnits(),
        formDialog,
        ::MeasurementUnitListState,
    )

    override suspend fun handleIntent(intent: MeasurementUnitListIntent) {
        when (intent) {
            is MeasurementUnitListIntent.Close ->
                popScreen()
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