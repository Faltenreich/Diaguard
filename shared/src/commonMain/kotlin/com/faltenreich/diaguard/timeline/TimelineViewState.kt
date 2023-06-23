package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.Date

sealed class TimelineViewState(val date: Date) {

    class Requesting(date: Date) : TimelineViewState(date)

    class Responding(
        date: Date,
        val bloodSugarList: List<MeasurementValue>,
    ) : TimelineViewState(date)
}