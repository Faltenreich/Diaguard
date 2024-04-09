package com.faltenreich.diaguard.measurement.unit.list

import com.faltenreich.diaguard.measurement.property.form.UpdateMeasurementPropertyUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MeasurementUnitListViewModel(
    private val updateMeasurementProperty: UpdateMeasurementPropertyUseCase = inject(),
) : ViewModel<Unit, MeasurementUnitListIntent, Unit>() {

    override val state: Flow<Unit>
        get() = flowOf(Unit)

    override fun handleIntent(intent: MeasurementUnitListIntent) {
        when (intent) {
            is MeasurementUnitListIntent.Select -> {
                val unit = intent.unit
                val property = unit.property.copy(selectedUnitId = unit.id)
                updateMeasurementProperty(property)
            }
        }
    }
}