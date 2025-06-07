package com.faltenreich.diaguard.timeline.canvas.chart

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlin.math.max

class GetTimelineChartDataUseCase {

    operator fun invoke(
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


    companion object {

        private const val Y_AXIS_MIN = 0.0
        private const val Y_AXIS_STEP = 50.0
        private const val Y_AXIS_MAX_MIN = 250.0
    }
}