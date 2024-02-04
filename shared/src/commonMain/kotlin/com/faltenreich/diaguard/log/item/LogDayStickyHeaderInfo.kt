package com.faltenreich.diaguard.log.item

import androidx.compose.ui.unit.IntOffset
import com.faltenreich.diaguard.shared.datetime.Date

data class LogDayStickyHeaderInfo(
    val date: Date? = null,
    val style: LogDayStyle = LogDayStyle(isVisible = false, isHighlighted = false),
    val offset: IntOffset = IntOffset.Zero,
    val clip: Float = 0f,
)