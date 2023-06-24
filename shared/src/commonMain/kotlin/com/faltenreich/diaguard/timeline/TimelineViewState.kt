package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.Date

sealed class TimelineViewState(
    val initialDate: Date,
    val currentDate: Date,
    val bloodSugarList: List<MeasurementValue>,
) {

    class Requesting(
        initialDate: Date,
        currentDate: Date,
    ) : TimelineViewState(initialDate, currentDate, emptyList())

    class Responding(
        initialDate: Date,
        currentDate: Date,
        bloodSugarList: List<MeasurementValue>,
    ) : TimelineViewState(initialDate, currentDate, bloodSugarList)
}