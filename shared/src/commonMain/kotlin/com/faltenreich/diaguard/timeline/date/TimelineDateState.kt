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
    val hours: List<Hour>,
) {

    data class Hour(
        val x: Float,
        val hour: Int,
    )
}