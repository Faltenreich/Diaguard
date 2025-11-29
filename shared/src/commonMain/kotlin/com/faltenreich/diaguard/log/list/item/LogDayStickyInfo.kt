package com.faltenreich.diaguard.log.list.item

import androidx.compose.ui.unit.IntOffset

data class LogDayStickyInfo(
    val dayState: LogDayState,
    val offset: IntOffset = IntOffset.Zero,
)