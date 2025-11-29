package com.faltenreich.diaguard.statistic.distribution

import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.measurement.value.MeasurementValueTint
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.no_entries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlin.math.round

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
        val label = round(percentage * 100f).toInt().toString()
        return StatisticDistributionState.Part(
            label = label,
            percentage = percentage,
            tint = tint,
        )
    }
}