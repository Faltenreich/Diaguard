package com.faltenreich.diaguard.dashboard.usecase

import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetLatestBloodSugarUseCase(
    private val measurementValueRepository: MeasurementValueRepository = inject(),
) {

    operator fun invoke(): Flow<DashboardViewState.Revisit.BloodSugar?> {
        return measurementValueRepository.observeLatestByPropertyId(
            propertyId = MeasurementProperty.BLOOD_SUGAR_ID,
        ).map { value ->
            when (value) {
                null -> null
                else -> DashboardViewState.Revisit.BloodSugar(value)
            }
        }
    }
}