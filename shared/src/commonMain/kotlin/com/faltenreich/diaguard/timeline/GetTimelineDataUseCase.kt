package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTimelineDataUseCase(
    private val valueRepository: MeasurementValueRepository,
    private val categoryRepository: MeasurementCategoryRepository,
    private val numberFormatter: NumberFormatter,
) {

    operator fun invoke(date: Date): Flow<TimelineData> {
        return valueRepository.observeByDateRange(
            startDateTime = date.minus(2, DateUnit.DAY).atStartOfDay(),
            endDateTime = date.plus(2, DateUnit.DAY).atEndOfDay(),
        ).map { values ->
            val valuesForChart = values
                .filter { value -> value.property.category.isBloodSugar }
                .map { value ->
                    TimelineData.Chart.Value(
                        dateTime = value.entry.dateTime,
                        value = value.value, // TODO: Map to MeasurementValueForUser?
                    )
                }
            val valuesForTable = values.filterNot { value -> value.property.category.isBloodSugar }
            val categories = categoryRepository.getAll().filterNot(MeasurementCategory::isBloodSugar)
            TimelineData(
                chart = TimelineData.Chart(valuesForChart),
                table = TimelineData.Table(
                    rows = categories.map { category ->
                        TimelineData.Table.Row(
                            category = category,
                            values = valuesForTable
                                .filter { it.property.category == category }
                                .groupBy { value ->
                                    val hour = value.entry.dateTime.time.hourOfDay
                                    val hourNormalized = hour - (hour % TimelineConfig.STEP)
                                    value.entry.dateTime.copy(
                                        hourOfDay = hourNormalized,
                                        minuteOfHour = 0,
                                        secondOfMinute = 0,
                                        millisOfSecond = 0,
                                        nanosOfMilli = 0,
                                    )
                                }
                                .map { (dateTime, values) ->
                                    // TODO: Determine whether it is sum or average
                                    val sum = values.sumOf { it.value }
                                    TimelineData.Table.Row.Value(
                                        dateTime = dateTime,
                                        value = numberFormatter(sum),
                                    )
                                },
                        )
                    },
                )
            )
        }
    }
}