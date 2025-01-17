package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.primitive.format
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

class GetAverageUseCase(
    private val repository: MeasurementValueRepository,
    private val getPreference: GetPreferenceUseCase,
    private val mapValue: MeasurementValueMapper,
) {

    operator fun invoke(
        category: MeasurementCategory.Local,
        dateRange: ClosedRange<Date>,
    ): Flow<StatisticState.Average> {
        return combine(
            repository.observeAveragesByCategoryId(
                categoryId = category.id,
                minDateTime = dateRange.start.atStartOfDay(),
                maxDateTime = dateRange.endInclusive.atEndOfDay(),
            ),
            // TODO: Convert to Flow
            flowOf(repository.countByCategoryId(category.id)),
            getPreference(DecimalPlacesPreference),
        ) { averages, countPerDay, decimalPlaces ->
            StatisticState.Average(
                values = averages.map { average ->
                    average.property to
                        "%s %s".format(
                            mapValue(
                                value = average.value,
                                unit = average.property.selectedUnit,
                                decimalPlaces = decimalPlaces,
                            ).value,
                            average.property.selectedUnit.abbreviation,
                        )
                },
                countPerDay = countPerDay.toString(),
            )
        }
    }
}