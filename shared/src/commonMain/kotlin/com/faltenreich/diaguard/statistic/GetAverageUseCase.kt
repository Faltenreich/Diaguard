package com.faltenreich.diaguard.statistic

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import com.faltenreich.diaguard.shared.primitive.format

class GetAverageUseCase(
    private val typeRepository: MeasurementTypeRepository,
    private val valueRepository: MeasurementValueRepository,
    private val formatNumber: NumberFormatter,
) {

    operator fun invoke(
        property: MeasurementProperty,
        dateRange: ClosedRange<Date>,
    ): StatisticViewState.Loaded.Average {
        return StatisticViewState.Loaded.Average(
            values = typeRepository.getByPropertyId(property.id).map { type ->
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
            countPerDay = valueRepository.countByPropertyId(property.id).toString(),
        )
    }
}