package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import com.faltenreich.diaguard.shared.primitive.format

class GetAverageUseCase(
    private val propertyRepository: MeasurementPropertyRepository,
    private val valueRepository: MeasurementValueRepository,
    private val formatNumber: NumberFormatter,
) {

    operator fun invoke(
        category: MeasurementCategory.Local,
        dateRange: ClosedRange<Date>,
    ): StatisticViewState.Loaded.Average {
        return StatisticViewState.Loaded.Average(
            values = propertyRepository.getByCategoryId(category.id).map { property ->
                property to valueRepository.getAverageByPropertyId(
                    propertyId = property.id,
                    minDateTime = dateRange.start.atStartOfDay(),
                    dateRange.endInclusive.atEndOfDay(),
                )?.let { average ->
                    "%s %s".format(
                        formatNumber(average),
                        property.selectedUnit.abbreviation,
                    )
                }
            },
            countPerDay = valueRepository.countByCategoryId(category.id).toString(),
        )
    }
}