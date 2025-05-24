package com.faltenreich.diaguard.statistic.distribution

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.measurement.value.tint.MeasurementValueTint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class GetStatisticDistributionUseCase(
    private val propertyRepository: MeasurementPropertyRepository,
    private val valueRepository: MeasurementValueRepository,
) {

    operator fun invoke(
        category: MeasurementCategory.Local,
        dateRange: ClosedRange<Date>,
    ): Flow<StatisticDistributionState> {
        return propertyRepository.observeByCategoryId(category.id).flatMapLatest { properties ->
            val observeProperties = properties.map { property -> invoke(property, dateRange) }
            combine(observeProperties) {
                StatisticDistributionState(properties = it.toList())
            }
        }
    }

    private fun invoke(
        property: MeasurementProperty.Local,
        dateRange: ClosedRange<Date>,
    ): Flow<StatisticDistributionState.Property> {
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
                        percentage = targetCount / totalCount,
                        tint = MeasurementValueTint.NORMAL,
                    ),
                    StatisticDistributionState.Part(
                        percentage = lowCount / totalCount,
                        tint = MeasurementValueTint.LOW,
                    ),
                    StatisticDistributionState.Part(
                        percentage = highCount / totalCount,
                        tint = MeasurementValueTint.HIGH,
                    ),
                )
            } else {
                emptyList()
            }
            StatisticDistributionState.Property(
                property = property,
                parts = parts,
            )
        }
    }
}