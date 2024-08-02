package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.primitive.format

class GetAverageUseCase(
    private val propertyRepository: MeasurementPropertyRepository,
    private val valueRepository: MeasurementValueRepository,
    private val mapValue: MeasurementValueMapper,
) {

    operator fun invoke(
        category: MeasurementCategory.Local,
        dateRange: ClosedRange<Date>,
        decimalPlaces: Int,
    ): StatisticViewState.Average {
        return StatisticViewState.Average(
            values = propertyRepository.getByCategoryId(category.id).map { property ->
                property to valueRepository.getAverageByPropertyId(
                    propertyId = property.id,
                    minDateTime = dateRange.start.atStartOfDay(),
                    dateRange.endInclusive.atEndOfDay(),
                )?.let { average ->
                    val unit = property.selectedUnit
                    "%s %s".format(
                        mapValue(
                            value = average,
                            unit = unit,
                            decimalPlaces = decimalPlaces,
                        ).value,
                        unit.abbreviation,
                    )
                }
            },
            countPerDay = valueRepository.countByCategoryId(category.id).toString(),
        )
    }
}