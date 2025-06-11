package com.faltenreich.diaguard.timeline.date

import androidx.compose.ui.geometry.Rect
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateTime

data class TimelineDateState(
    val rectangle: Rect,
    val initialDate: Date,
    val initialDateTime: DateTime,
    val currentDate: Date,
    val currentDateLocalized: String,
    val axis: Axis,
    val hours: List<Hour>,
) {

    data class Hour(
        val x: Float,
        val hour: Int,
    )

    data class Axis(
        private val minimum: Int,
        private val maximum: Int,
        private val step: Int,
    ) {

        val progression: IntProgression = minimum .. maximum step step
    }
}