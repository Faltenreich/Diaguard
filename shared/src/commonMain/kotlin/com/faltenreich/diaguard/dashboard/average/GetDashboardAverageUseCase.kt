package com.faltenreich.diaguard.dashboard.average

import com.faltenreich.diaguard.dashboard.DashboardState
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetDashboardAverageUseCase(
    private val propertyRepository: MeasurementPropertyRepository,
    private val valueRepository: MeasurementValueRepository,
    private val mapValue: MeasurementValueMapper,
    private val getToday: GetTodayUseCase,
    private val getPreference: GetPreferenceUseCase,
) {

    operator fun invoke(): Flow<DashboardState.Average> {
        val today = getToday()
        val todayAtEndOfDay = today.atEndOfDay()

        return combine(
            propertyRepository.observeByCategoryKey(DatabaseKey.MeasurementCategory.BLOOD_SUGAR),
            valueRepository.observeAverageByPropertyKey(
                propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
                minDateTime = today.atStartOfDay(),
                maxDateTime = todayAtEndOfDay,
            ),
            valueRepository.observeAverageByPropertyKey(
                propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
                minDateTime = today.minus(1, DateUnit.WEEK).atStartOfDay(),
                maxDateTime = todayAtEndOfDay,
            ),
            valueRepository.observeAverageByPropertyKey(
                propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
                minDateTime = today.minus(1, DateUnit.MONTH).atStartOfDay(),
                maxDateTime = todayAtEndOfDay,
            ),
            getPreference(DecimalPlacesPreference),
        ) { properties, averageOfDay, averageOfWeek, averageOfMonth, decimalPlaces ->
            val property = properties.firstOrNull() ?: return@combine DashboardState.Average(
                day = null,
                week = null,
                month = null,
            )
            DashboardState.Average(
                day = averageOfDay?.let {
                    mapValue(
                        value = averageOfDay,
                        property = property,
                        decimalPlaces = decimalPlaces,
                    )
                },
                week = averageOfWeek?.let {
                    mapValue(
                        value = averageOfWeek,
                        property = property,
                        decimalPlaces = decimalPlaces,
                    )
                },
                month = averageOfMonth?.let {
                    mapValue(
                        value = averageOfMonth,
                        property = property,
                        decimalPlaces = decimalPlaces,
                    )
                },
            )
        }
    }
}