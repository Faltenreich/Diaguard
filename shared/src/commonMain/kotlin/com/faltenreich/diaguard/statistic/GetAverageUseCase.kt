package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import com.faltenreich.diaguard.shared.primitive.format

class GetAverageUseCase(
    private val typeRepository: MeasurementTypeRepository,
    private val valueRepository: MeasurementValueRepository,
    private val formatNumber: NumberFormatter,
) {

    operator fun invoke(
        category: MeasurementCategory,
        dateRange: ClosedRange<Date>,
    ): StatisticViewState.Loaded.Average {
        return StatisticViewState.Loaded.Average(
            values = typeRepository.getByCategoryId(category.id).map { type ->
                type to valueRepository.getAverageByTypeId(
                    typeId = type.id,
                    minDateTime = dateRange.start.atStartOfDay(),
                    dateRange.endInclusive.atEndOfDay(),
                )?.let { average ->
                    "%s %s".format(
                        formatNumber(average),
                        type.selectedUnit.abbreviation,
                    )
                }
            },
            countPerDay = valueRepository.countByCategoryId(category.id).toString(),
        )
    }
}