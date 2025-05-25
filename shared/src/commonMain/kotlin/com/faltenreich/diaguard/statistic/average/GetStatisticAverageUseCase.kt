package com.faltenreich.diaguard.statistic.average

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.localization.format
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
        return combine(
            valueRepository.observeAverageByPropertyId(
                propertyId = property.id,
                minDateTime = dateRange.start.atStartOfDay(),
                maxDateTime = dateRange.endInclusive.atEndOfDay(),
            ),
            // TODO: ByPropertyId
            valueRepository.observeCountByCategoryId(property.id),
            getPreference(DecimalPlacesPreference),
        ) { average, countPerDay, decimalPlaces ->
            StatisticAverageState(
                property = property,
                value = average?.let {
                    "%s %s".format(
                        mapValue(
                            value = average,
                            property = property,
                            decimalPlaces = decimalPlaces,
                        ).value,
                        property.unit.abbreviation,
                    )
                },
                countPerDay = countPerDay.toString(),
            )
        }
    }
}