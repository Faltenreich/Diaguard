package com.faltenreich.diaguard.dashboard.usecase

import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.datetime.Time
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTodayUseCase(
    private val measurementValueRepository: MeasurementValueRepository = inject(),
    private val dateTimeFactory: DateTimeFactory = inject(),
) {

    operator fun invoke(): Flow<DashboardViewState.Revisit.Today> {
        return measurementValueRepository.observeByPropertyId(
            propertyId = MeasurementProperty.BLOOD_SUGAR_ID,
            minDateTime = dateTimeFactory.today().atTime(Time.atStartOfDay()),
            maxDateTime = dateTimeFactory.today().atTime(Time.atEndOfDay()),
        ).map { values: List<MeasurementValue> ->
            DashboardViewState.Revisit.Today(
                totalCount = values.size,
                hyperCount = 0,
                hypoCount = 0,
            )
        }
    }
}