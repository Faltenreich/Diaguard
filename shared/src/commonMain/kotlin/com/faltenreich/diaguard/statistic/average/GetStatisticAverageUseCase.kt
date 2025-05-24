package com.faltenreich.diaguard.statistic.average

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.localization.format
import com.faltenreich.diaguard.statistic.StatisticState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class GetStatisticAverageUseCase(
    private val propertyRepository: MeasurementPropertyRepository,
    private val valueRepository: MeasurementValueRepository,
    private val getPreference: GetPreferenceUseCase,
    private val mapValue: MeasurementValueMapper,
) {

    operator fun invoke(
        category: MeasurementCategory.Local,
        dateRange: ClosedRange<Date>,
    ): Flow<StatisticState.Average> {
        return combine(
            propertyRepository.observeByCategoryId(category.id).flatMapLatest { properties ->
                combine(
                    properties.map { property ->
                        valueRepository.observeAverageByPropertyId(
                            propertyId = property.id,
                            minDateTime = dateRange.start.atStartOfDay(),
                            maxDateTime = dateRange.endInclusive.atEndOfDay(),
                        ).map { average -> property to average }
                    }
                ) { it }
            },
            valueRepository.observeCountByCategoryId(category.id),
            getPreference(DecimalPlacesPreference),
        ) { averages, countPerDay, decimalPlaces ->
            StatisticState.Average(
                values = averages.map { (property, average) ->
                    property to average?.let {
                        "%s %s".format(
                            mapValue(
                                value = average,
                                property = property,
                                decimalPlaces = decimalPlaces,
                            ).value,
                            property.unit.abbreviation,
                        )
                    }
                },
                countPerDay = countPerDay.toString(),
            )
        }
    }
}