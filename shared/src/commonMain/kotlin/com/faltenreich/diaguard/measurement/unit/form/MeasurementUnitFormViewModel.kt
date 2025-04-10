package com.faltenreich.diaguard.measurement.unit.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.StoreMeasurementUnitUseCase
import com.faltenreich.diaguard.navigation.modal.CloseModalUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.emptyFlow

class MeasurementUnitFormViewModel(
    unitId: Long,
    getUnit: GetMeasurementUnitUseCase = inject(),
    private val storeUnit: StoreMeasurementUnitUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
) : ViewModel<Unit, MeasurementUnitFormIntent, Unit>() {

    override val state = emptyFlow<Unit>()

    private val unit = getUnit(unitId)

    var name: String by mutableStateOf(unit?.name ?: "")
    var abbreviation: String by mutableStateOf(unit?.abbreviation ?: "")

    override suspend fun handleIntent(intent: MeasurementUnitFormIntent) {
        when (intent) {
            is MeasurementUnitFormIntent.Close -> closeModal()
            is MeasurementUnitFormIntent.Submit -> submit()
        }
    }

    private suspend fun submit() {
        val unit = unit?.copy(
            name = name,
            abbreviation = abbreviation,
        ) ?: MeasurementUnit.User(
            name = name,
            abbreviation = abbreviation
        )
        storeUnit(unit)
        closeModal()
    }
}