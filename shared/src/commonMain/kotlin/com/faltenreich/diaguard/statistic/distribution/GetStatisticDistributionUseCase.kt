package com.faltenreich.diaguard.statistic.distribution

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.measurement.value.tint.MeasurementValueTint
import com.faltenreich.diaguard.shared.localization.Localization
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.value_range_high
import diaguard.shared.generated.resources.value_range_low
import diaguard.shared.generated.resources.value_range_target
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class GetStatisticDistributionUseCase(
    private val valueRepository: MeasurementValueRepository,
    private val localization: Localization,
) {

    operator fun invoke(
        property: MeasurementProperty.Local,
        dateRange: ClosedRange<Date>,
    ): Flow<StatisticDistributionState> {
        val minDateTime = dateRange.start.atStartOfDay()
        val maxDateTime = dateRange.endInclusive.atEndOfDay()

        val minimum = property.range.minimum
        val low = property.range.low ?: property.range.minimum
        val high = property.range.high ?: property.range.maximum
        val maximum = property.range.maximum

        return combine(
            valueRepository.observeCountByValueRange(
                range = minimum .. low,
                propertyId = property.id,
                minDateTime = minDateTime,
                maxDateTime = maxDateTime,
            ).map(Long::toFloat),
            valueRepository.observeCountByValueRange(
                range = low .. high,
                propertyId = property.id,
                minDateTime = minDateTime,
                maxDateTime = maxDateTime,
            ).map(Long::toFloat),
            valueRepository.observeCountByValueRange(
                range = high .. maximum,
                propertyId = property.id,
                minDateTime = minDateTime,
                maxDateTime = maxDateTime,
            ).map(Long::toFloat),
        ) { lowCount, targetCount, highCount ->
            val totalCount = lowCount + targetCount + highCount
            val parts = if (totalCount > 0) {
                listOf(
                    StatisticDistributionState.Part(
                        label = localization.getString(Res.string.value_range_target),
                        percentage = targetCount / totalCount,
                        tint = MeasurementValueTint.NORMAL,
                    ),
                    StatisticDistributionState.Part(
                        label = localization.getString(Res.string.value_range_low),
                        percentage = lowCount / totalCount,
                        tint = MeasurementValueTint.LOW,
                    ),
                    StatisticDistributionState.Part(
                        label = localization.getString(Res.string.value_range_high),
                        percentage = highCount / totalCount,
                        tint = MeasurementValueTint.HIGH,
                    ),
                )
            } else {
                emptyList()
            }
            StatisticDistributionState(
                property = property,
                parts = parts,
            )
        }
    }
}