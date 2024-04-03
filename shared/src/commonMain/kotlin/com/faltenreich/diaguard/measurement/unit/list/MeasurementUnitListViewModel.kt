package com.faltenreich.diaguard.measurement.unit.list

import com.faltenreich.diaguard.measurement.type.form.UpdateMeasurementTypeUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MeasurementUnitListViewModel(
    private val updateMeasurementType: UpdateMeasurementTypeUseCase = inject(),
) : ViewModel<Unit, MeasurementUnitListIntent>() {

    override val state: Flow<Unit>
        get() = flowOf(Unit)

    override fun handleIntent(intent: MeasurementUnitListIntent) {
        when (intent) {
            is MeasurementUnitListIntent.Select -> {
                val unit = intent.unit
                val type = unit.type.copy(selectedUnitId = unit.id)
                updateMeasurementType(type)
            }
        }
    }
}