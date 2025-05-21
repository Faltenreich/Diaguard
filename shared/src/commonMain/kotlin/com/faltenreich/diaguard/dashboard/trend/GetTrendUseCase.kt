package com.faltenreich.diaguard.dashboard.trend

import com.faltenreich.diaguard.dashboard.DashboardState
import com.faltenreich.diaguard.datetime.DateProgression
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class GetTrendUseCase(
    private val propertyRepository: MeasurementPropertyRepository,
    private val valueRepository: MeasurementValueRepository,
    private val dateTimeFactory: DateTimeFactory,
    private val dateTimeFormatter: DateTimeFormatter,
) {

    operator fun invoke(): Flow<DashboardState.Trend> {
        val key = DatabaseKey.MeasurementProperty.BLOOD_SUGAR

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
            averagesByDate.map { (date, average) ->
                DashboardState.Trend.Day(
                    date = dateTimeFormatter.formatDayOfWeek(date, abbreviated = true),
                    average = average,
                )
            }
        }.map { days ->
            DashboardState.Trend(
                days = days,
                // TODO: Use real values
                targetValue = 120.0,
                maximumValue = days
                    .mapNotNull(DashboardState.Trend.Day::average)
                    .maxOrNull()
                    ?: 200.0,
            )
        }
    }
}