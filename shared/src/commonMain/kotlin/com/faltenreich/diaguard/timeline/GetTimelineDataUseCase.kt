package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.usecase.GetActiveMeasurementCategoriesWithPropertiesUseCase
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.aggregationstyle.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.usecase.GetMeasurementValuesInDateRangeUseCase
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.timeline.canvas.chart.TimelineChartState
import com.faltenreich.diaguard.timeline.canvas.table.TimelineTableState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlin.math.max

class GetTimelineDataUseCase(
    private val getValues: GetMeasurementValuesInDateRangeUseCase,
    private val getCategories: GetActiveMeasurementCategoriesWithPropertiesUseCase,
    private val getPreference: GetPreferenceUseCase,
    private val mapValue: MeasurementValueMapper,
) {

    operator fun invoke(date: Date): Flow<TimelineData> {
        val dateRange = date.minus(2, DateUnit.DAY) .. date.plus(2, DateUnit.DAY)
        return combine(
            getValues(dateRange),
            getCategories(),
            getPreference(DecimalPlacesPreference),
        ) { values, categoriesWithProperties, decimalPlaces ->
            val categories = categoriesWithProperties.keys.toList()
            val properties = categoriesWithProperties.values.flatten()
            TimelineData(
                chart = getChartData(values, properties),
                table = getTableData(values, properties, categories, decimalPlaces),
            )
        }
    }

    private fun getChartData(
        values: List<MeasurementValue.Local>,
        properties: List<MeasurementProperty.Local>,
    ): TimelineChartState {
        val property = properties.first { it.key == DatabaseKey.MeasurementProperty.BLOOD_SUGAR }
        val valuesOfProperty = values
            .filter { it.property.category.isBloodSugar }
            .map { value ->
                TimelineChartState.Value(
                    dateTime = value.entry.dateTime,
                    value = value.value,
                )
            }
        return TimelineChartState(
            values = valuesOfProperty,
            valueMin = Y_AXIS_MIN,
            valueLow = property.range.low,
            valueHigh = property.range.high,
            valueMax = max(
                Y_AXIS_MAX_MIN,
                (valuesOfProperty.maxOfOrNull { it.value } ?: 0.0) + Y_AXIS_STEP,
            ),
            valueStep = Y_AXIS_STEP,
        )
    }

    private fun getTableData(
        values: List<MeasurementValue.Local>,
        properties: List<MeasurementProperty.Local>,
        categories: List<MeasurementCategory.Local>,
        decimalPlaces: Int,
    ): TimelineTableState {
        val valuesForTable = values.filterNot { value -> value.property.category.isBloodSugar }
        return TimelineTableState(
            categories = categories
                .filterNot { it.isBloodSugar }
                .map { category ->
                    getTableCategory(valuesForTable, properties, category, decimalPlaces)
                },
        )
    }

    private fun getTableCategory(
        values: List<MeasurementValue.Local>,
        properties: List<MeasurementProperty.Local>,
        category: MeasurementCategory,
        decimalPlaces: Int,
    ): TimelineTableState.Category {
        val propertiesOfCategory = properties.filter { it.category == category }
        return TimelineTableState.Category(
            icon = category.icon,
            name = category.name,
            properties = propertiesOfCategory.map { property ->
                getTableProperty(values, property, decimalPlaces)
            }
        )
    }

    private fun getTableProperty(
        values: List<MeasurementValue.Local>,
        property: MeasurementProperty.Local,
        decimalPlaces: Int,
    ): TimelineTableState.Category.Property {
        return TimelineTableState.Category.Property(
            name = property.name,
            values = values
                .filter { it.property == property }
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
                    getTableValue(values, property, dateTime, decimalPlaces)
                },
        )
    }

    private fun getTableValue(
        values: List<MeasurementValue.Local>,
        property: MeasurementProperty.Local,
        dateTime: DateTime,
        decimalPlaces: Int,
    ): TimelineTableState.Category.Value {
        val sum = values.sumOf { it.value }
        val value = when (property.aggregationStyle) {
            MeasurementAggregationStyle.CUMULATIVE -> sum
            MeasurementAggregationStyle.AVERAGE -> sum / values.size
        }
        return TimelineTableState.Category.Value(
            dateTime = dateTime,
            // TODO: Decimal places may take up too much space
            value = mapValue(
                value = value,
                property = property,
                decimalPlaces = decimalPlaces,
            ).value,
        )
    }

    companion object {

        private const val Y_AXIS_MIN = 0.0
        private const val Y_AXIS_STEP = 50.0
        private const val Y_AXIS_MAX_MIN = 250.0
    }
}