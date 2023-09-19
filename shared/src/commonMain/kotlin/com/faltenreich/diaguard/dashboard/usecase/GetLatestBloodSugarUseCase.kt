package com.faltenreich.diaguard.dashboard.usecase

import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValueFormatter
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetLatestBloodSugarUseCase(
    private val measurementValueRepository: MeasurementValueRepository = inject(),
    private val valueFormatter: MeasurementValueFormatter = inject(),
) {

    operator fun invoke(): Flow<DashboardViewState.Revisit.LatestBloodSugar?> {
        return measurementValueRepository.observeLatestByPropertyId(MeasurementProperty.BLOOD_SUGAR_ID).map { value ->
            when (value) {
                null -> null
                else -> DashboardViewState.Revisit.LatestBloodSugar(
                    entry = value.entry,
                    value = valueFormatter.formatValue(value),
                )
            }
        }
    }
}