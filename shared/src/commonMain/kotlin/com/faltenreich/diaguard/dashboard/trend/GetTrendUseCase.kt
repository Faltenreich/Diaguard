package com.faltenreich.diaguard.dashboard.trend

import com.faltenreich.diaguard.dashboard.DashboardState
import com.faltenreich.diaguard.datetime.DateProgression
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class GetTrendUseCase(
    private val propertyRepository: MeasurementPropertyRepository,
    private val valueRepository: MeasurementValueRepository,
    private val dateTimeFactory: DateTimeFactory,
) {

    operator fun invoke(): Flow<DashboardState.Trend> {
        val key = DatabaseKey.MeasurementProperty.BLOOD_SUGAR
        // TODO: Observe by key
        val property = propertyRepository.getAll().first { it.key == key }

        val today = dateTimeFactory.today()
        val dateRange = today.minus(1, DateUnit.WEEK) .. today

        return combine(
            DateProgression(dateRange.start, dateRange.endInclusive).map { date ->
                valueRepository.observeAverageByPropertyKey(
                    propertyKey = key,
                    minDateTime = date.atStartOfDay(),
                    maxDateTime = date.atEndOfDay(),
                ).map { date to it }
            }
        ) { averagesByDate ->
            averagesByDate
                .toMap()
                .mapValues { (_, average) ->
                    average ?: return@mapValues null
                    MeasurementValue.Average(
                        value = average,
                        property = property,
                    )
                }
        }.map { values ->
            DashboardState.Trend(values = values)
        }
    }
}