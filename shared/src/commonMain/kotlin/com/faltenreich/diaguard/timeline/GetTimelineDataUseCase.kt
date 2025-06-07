package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.property.aggregationstyle.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlin.math.max

private const val Y_AXIS_MIN = 0.0
private const val Y_AXIS_STEP = 50.0
private const val Y_AXIS_MAX_MIN = 250.0

class GetTimelineDataUseCase(
    private val categoryRepository: MeasurementCategoryRepository,
    private val propertyRepository: MeasurementPropertyRepository,
    private val valueRepository: MeasurementValueRepository,
    private val getPreference: GetPreferenceUseCase,
    private val mapValue: MeasurementValueMapper,
) {

    operator fun invoke(date: Date): Flow<TimelineData> {
        return combine(
            getActiveCategoriesWithProperties(),
            getPreference(DecimalPlacesPreference),
            valueRepository.observeByDateRange(
                startDateTime = date.minus(2, DateUnit.DAY).atStartOfDay(),
                endDateTime = date.plus(2, DateUnit.DAY).atEndOfDay(),
            ),
        ) { categoriesWithProperties, decimalPlaces, values ->
            val categories = categoriesWithProperties.keys
            val properties = categoriesWithProperties.values.flatten()
            val bloodSugarProperty = properties.first {
                it.key == DatabaseKey.MeasurementProperty.BLOOD_SUGAR
            }
            val valuesForChart = values
                .filter { value -> value.property.category.isBloodSugar }
                .map { value ->
                    TimelineData.Chart.Value(
                        dateTime = value.entry.dateTime,
                        value = value.value,
                    )
                }
            val valuesForTable = values.filterNot { value -> value.property.category.isBloodSugar }
            val valueForChartMax = valuesForChart.maxOfOrNull { it.value } ?: 0.0

            TimelineData(
                chart = TimelineData.Chart(
                    values = valuesForChart,
                    valueMin = Y_AXIS_MIN,
                    valueLow = bloodSugarProperty.range.low,
                    valueHigh = bloodSugarProperty.range.high,
                    valueMax = max(Y_AXIS_MAX_MIN, valueForChartMax + Y_AXIS_STEP),
                    valueStep = Y_AXIS_STEP,
                ),
                table = TimelineData.Table(
                    categories = categories
                        .filterNot { it.isBloodSugar }
                        .map { category ->
                            val propertiesOfCategory = properties.filter { it.category == category }
                            TimelineData.Table.Category(
                                icon = category.icon,
                                name = category.name,
                                properties = propertiesOfCategory.map { property ->
                                    TimelineData.Table.Category.Property(
                                        name = property.name,
                                        values = valuesForTable
                                            .filter { it.property == property }
                                            .groupBy { value ->
                                                val hour = value.entry.dateTime.time.hourOfDay
                                                val hourNormalized =
                                                    hour - (hour % TimelineConfig.STEP)
                                                value.entry.dateTime.copy(
                                                    hourOfDay = hourNormalized,
                                                    minuteOfHour = 0,
                                                    secondOfMinute = 0,
                                                    millisOfSecond = 0,
                                                    nanosOfMilli = 0,
                                                )
                                            }
                                            .map { (dateTime, values) ->
                                                val sum = values.sumOf { it.value }
                                                val value = when (property.aggregationStyle) {
                                                    MeasurementAggregationStyle.CUMULATIVE -> sum
                                                    MeasurementAggregationStyle.AVERAGE -> sum / values.size
                                                }
                                                TimelineData.Table.Category.Value(
                                                    dateTime = dateTime,
                                                    // TODO: Decimal places may take up too much space
                                                    value = mapValue(
                                                        value = value,
                                                        property = property,
                                                        decimalPlaces = decimalPlaces,
                                                    ).value,
                                                )
                                            },
                                    )
                                }
                            )
                        },
                )
            )
        }
    }

    private fun getActiveCategoriesWithProperties():
        Flow<Map<MeasurementCategory.Local, List<MeasurementProperty.Local>>> {
        return categoryRepository.observeActive().flatMapLatest { categories ->
            combine(
                categories.map { category ->
                    propertyRepository.observeByCategoryId(category.id).map { properties ->
                        category to properties
                    }
                },
                Array<Pair<MeasurementCategory.Local, List<MeasurementProperty.Local>>>::toMap,
            )
        }
    }
}