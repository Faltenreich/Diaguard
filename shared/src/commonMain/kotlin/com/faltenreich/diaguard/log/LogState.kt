package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.log.item.LogDayStickyHeaderInfo

data class LogState(
    val monthHeaderHeight: Int,
    val dayHeaderHeight: Int,
    val stickyDayInfo: LogDayStickyHeaderInfo,
)