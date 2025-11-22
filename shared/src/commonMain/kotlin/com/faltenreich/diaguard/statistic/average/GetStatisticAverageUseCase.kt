package com.faltenreich.diaguard.statistic.average

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetStatisticAverageUseCase(
    private val valueRepository: MeasurementValueRepository,
    private val getPreference: GetPreferenceUseCase,
    private val mapValue: MeasurementValueMapper,
) {

    operator fun invoke(
        property: MeasurementProperty.Local,
        dateRange: ClosedRange<Date>,
    ): Flow<StatisticAverageState> {
        val minDateTime = dateRange.start.atStartOfDay()
        val maxDateTime = dateRange.endInclusive.atEndOfDay()
        return combine(
            getPreference(DecimalPlacesPreference),
            valueRepository.observeCountByPropertyId(
                propertyId = property.id,
                minDateTime = minDateTime,
                maxDateTime = maxDateTime,
            ),
            valueRepository.observeAverageByPropertyId(
                propertyId = property.id,
                minDateTime = minDateTime,
                maxDateTime = maxDateTime,
            ),
        ) { decimalPlaces, countPerDay, average ->
            StatisticAverageState(
                property = property,
                countPerDay = countPerDay.toString(),
                value = average?.let {
                    val value = mapValue(
                        value = average,
                        property = property,
                        decimalPlaces = decimalPlaces,
                    ).value
                    val unit = property.unit.abbreviation
                    "$value $unit"
                },
            )
        }
    }
}