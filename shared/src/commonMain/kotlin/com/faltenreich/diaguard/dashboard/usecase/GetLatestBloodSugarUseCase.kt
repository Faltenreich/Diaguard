package com.faltenreich.diaguard.dashboard.usecase

import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.datetime.DateTimeFormatter
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetLatestBloodSugarUseCase(
    private val measurementPropertyRepository: MeasurementPropertyRepository = inject(),
    private val measurementValueRepository: MeasurementValueRepository,
    private val measurementValueMapper: MeasurementValueMapper,
    private val dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(): Flow<DashboardViewState.Revisit.LatestBloodSugar?> {
        val property = measurementPropertyRepository.getBloodSugar()
        return measurementValueRepository.observeLatestByPropertyId(property.id).map { value ->
            when (value) {
                null -> null
                else -> DashboardViewState.Revisit.LatestBloodSugar(
                    entry = value.entry,
                    value = measurementValueMapper(value).value,
                    timePassed = dateTimeFormatter.formatTimePassed(
                        start = value.entry.dateTime,
                        end = dateTimeFactory.now(),
                    ),
                )
            }
        }
    }
}