package com.faltenreich.diaguard.statistic.distribution

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueTint
import com.faltenreich.diaguard.core.localization.Localization
import com.faltenreich.diaguard.core.localization.format
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.no_entries
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
                listOfNotNull(
                    getPart(lowCount, totalCount, MeasurementValueTint.LOW),
                    getPart(targetCount, totalCount, MeasurementValueTint.NORMAL),
                    getPart(highCount, totalCount, MeasurementValueTint.HIGH),
                )
            } else {
                listOf(
                    StatisticDistributionState.Part(
                        label = localization.getString(Res.string.no_entries),
                        percentage = 1f,
                        tint = MeasurementValueTint.NONE,
                    )
                )
            }
            StatisticDistributionState(
                property = property,
                parts = parts,
            )
        }
    }

    @Suppress("MagicNumber")
    private fun getPart(
        count: Float,
        totalCount: Float,
        tint: MeasurementValueTint,
    ): StatisticDistributionState.Part? {
        if (count == 0f) return null
        val percentage = if (totalCount > 0f) count / totalCount else 1f
        return StatisticDistributionState.Part(
            label = "%.0f %%".format((percentage) * 100f),
            percentage = percentage,
            tint = tint,
        )
    }
}