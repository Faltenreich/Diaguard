package com.faltenreich.diaguard.timeline.canvas.table

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.datetime.DateTimeConstants
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.property.aggregationstyle.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvasDimensions
import com.faltenreich.diaguard.timeline.canvas.time.TimelineTimeState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetTimelineTableStateUseCase(
    private val valueRepository: MeasurementValueRepository,
    private val propertyRepository: MeasurementPropertyRepository,
    private val getPreference: GetPreferenceUseCase,
    private val mapValue: MeasurementValueMapper,
) {

    operator fun invoke(
        time: TimelineTimeState,
        dimensions: TimelineCanvasDimensions,
        scrollOffset: Float,
    ): Flow<TimelineTableState> {
        return combine(
            valueRepository.observeByDateRangeIfCategoryIsActive(
                startDateTime = time.currentDate.minus(1, DateUnit.DAY).atStartOfDay(),
                endDateTime = time.currentDate.plus(1, DateUnit.DAY).atEndOfDay(),
                excludedPropertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
            ),
            propertyRepository.observeIfCategoryIsActive(
                excludedPropertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
            ),
            getPreference(DecimalPlacesPreference),
        ) { values, properties, decimalPlaces ->
            val categories = properties
                .mapNotNull(MeasurementProperty::category)
                .distinct()
                .sortedBy(MeasurementCategory::sortIndex)

            val rectangle = dimensions.table
            val rowSize = Size(
                width = rectangle.width,
                height = dimensions.table.height / properties.size,
            )
            var rowIndex = 0

            TimelineTableState(
                rectangle = dimensions.table,
                categories = categories.map { category ->
                    val propertiesOfCategory = properties.filter { it.category == category }
                    TimelineTableState.Category(
                        icon = category.icon,
                        name = category.name,
                        properties = propertiesOfCategory.map { property ->
                            val propertyRectangle = Rect(
                                offset = Offset(
                                    x = rectangle.left,
                                    y = rectangle.top + (rowIndex * rowSize.height),
                                ),
                                size = rowSize,
                            )

                            // TODO: Simplify
                            rowIndex++

                            TimelineTableState.Category.Property(
                                rectangle = propertyRectangle,
                                name = property.name,
                                values = values
                                    .filter { it.property == property }
                                    .groupBy { value ->
                                        val hour = value.entry.dateTime.time.hourOfDay
                                        val hourNormalized = hour - (hour % time.hourProgression.step)
                                        value.entry.dateTime.copy(
                                            hourOfDay = hourNormalized,
                                            minuteOfHour = 0,
                                            secondOfMinute = 0,
                                            millisOfSecond = 0,
                                            nanosOfMilli = 0,
                                        )
                                    }
                                    .map { (dateTime, values) ->
                                        val widthPerDay = rectangle.size.width
                                        val widthPerHour = widthPerDay /
                                            (time.hourProgression.last / time.hourProgression.step)
                                        val widthPerMinute = widthPerHour / DateTimeConstants.MINUTES_PER_HOUR

                                        val offsetInMinutes = time.initialDateTime.minutesUntil(dateTime)
                                        val offsetOfDateTime = (offsetInMinutes / time.hourProgression.step) *
                                            widthPerMinute
                                        val offsetOfHour = propertyRectangle.left + scrollOffset + offsetOfDateTime
                                        val valueRectangle = Rect(
                                            offset = Offset(
                                                x = offsetOfHour,
                                                y = propertyRectangle.top,
                                            ),
                                            size = Size(
                                                width = widthPerHour,
                                                height = propertyRectangle.height,
                                            )
                                        )

                                        val sum = values.sumOf { it.value }
                                        val value = when (property.aggregationStyle) {
                                            MeasurementAggregationStyle.CUMULATIVE -> sum
                                            MeasurementAggregationStyle.AVERAGE -> sum / values.size
                                        }

                                        TimelineTableState.Category.Value(
                                            rectangle = valueRectangle,
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
        }
    }
}