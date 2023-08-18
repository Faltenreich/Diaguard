package com.faltenreich.diaguard.dashboard.usecase

import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementTypeUnitRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class GetLatestBloodSugarUseCase(
    private val measurementValueRepository: MeasurementValueRepository = inject(),
    private val measurementTypeUnitRepository: MeasurementTypeUnitRepository = inject(),
) {

    operator fun invoke(): Flow<DashboardViewState.Revisit.LatestBloodSugar?> {
        return measurementValueRepository.observeLatestByPropertyId(MeasurementProperty.BLOOD_SUGAR_ID)
            .flatMapLatest { value ->
                when (value) {
                    null -> flowOf(emptyList())
                    else -> measurementTypeUnitRepository.getByTypeId(value.typeId)
                }.map { types -> value to types }
            }.map { (value, types) ->
                when (value) {
                    null -> null
                    else -> DashboardViewState.Revisit.LatestBloodSugar(
                        value = value,
                        unit = types.first(),
                    )
                }
            }
    }
}