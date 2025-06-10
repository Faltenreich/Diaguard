package com.faltenreich.diaguard.timeline.canvas.table

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.property.aggregationstyle.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.timeline.TimelineConfig
import com.faltenreich.diaguard.timeline.date.TimelineDateState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetTimelineTableStateUseCase(
    private val valueRepository: MeasurementValueRepository,
    private val propertyRepository: MeasurementPropertyRepository,
    private val getPreference: GetPreferenceUseCase,
    private val mapValue: MeasurementValueMapper,
) {

    operator fun invoke(dateState: TimelineDateState): Flow<TimelineTableState> {
        val dateRange = dateState.currentDate.minus(1, DateUnit.DAY) ..
            dateState.currentDate.plus(1, DateUnit.DAY)
        val excludedPropertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR
        return combine(
            valueRepository.observeByDateRangeIfCategoryIsActive(
                startDateTime = dateRange.start.atStartOfDay(),
                endDateTime = dateRange.endInclusive.atEndOfDay(),
                excludedPropertyKey = excludedPropertyKey,
            ),
            propertyRepository.observeIfCategoryIsActive(
                excludedPropertyKey = excludedPropertyKey,
            ),
            getPreference(DecimalPlacesPreference),
        ) { values, properties, decimalPlaces ->
            val categories = properties
                .mapNotNull(MeasurementProperty::category)
                .distinct()
                .sortedBy(MeasurementCategory::sortIndex)
            TimelineTableState(
                initialDateTime = dateState.initialDate.atStartOfDay(),
                categories = categories.map { category ->
                    getTableCategory(values, properties, category, decimalPlaces)
                },
            )
        }
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
}