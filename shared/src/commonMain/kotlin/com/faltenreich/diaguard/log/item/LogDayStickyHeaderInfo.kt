package com.faltenreich.diaguard.log.item

import androidx.compose.ui.unit.IntOffset
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.list.DateListItemStyle

data class LogDayStickyHeaderInfo(
    val date: Date? = null,
    val style: DateListItemStyle = DateListItemStyle(isVisible = false, isHighlighted = false),
    val offset: IntOffset = IntOffset.Zero,
    val clip: Float = 0f,
)