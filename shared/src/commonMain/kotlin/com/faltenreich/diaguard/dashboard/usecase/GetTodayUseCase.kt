package com.faltenreich.diaguard.dashboard.usecase

import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTodayUseCase(
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
    private val measurementValueRepository: MeasurementValueRepository = inject(),
    private val dateTimeFactory: DateTimeFactory = inject(),
) {

    operator fun invoke(): Flow<DashboardViewState.Revisit.Today> {
        val today = dateTimeFactory.today()
        val property = measurementPropertyRepository.getBloodSugar()
        return measurementValueRepository.observeByPropertyId(
            propertyId = property.id,
            minDateTime = today.atStartOfDay(),
            maxDateTime = today.atEndOfDay(),
        ).map { values: List<MeasurementValue> ->
            DashboardViewState.Revisit.Today(
                totalCount = values.size,
                hyperCount = 0, // TODO: Calculate count
                hypoCount = 0, // TODO: Calculate count
            )
        }
    }
}