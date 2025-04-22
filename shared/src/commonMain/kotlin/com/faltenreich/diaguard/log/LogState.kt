package com.faltenreich.diaguard.log

import androidx.compose.ui.unit.IntSize
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.log.item.LogDayStickyInfo

data class LogState(
    val monthHeaderSize: IntSize,
    val dayHeaderSize: IntSize,
    val dayStickyInfo: LogDayStickyInfo,
    val dateDialog: DateDialog?,
) {

    data class DateDialog(val date: Date)
}