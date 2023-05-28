package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.Month

data class LogViewState(
    val currentDate: Date,
    val items: Map<Month, List<LogItem>>,
    val scrollPosition: Int? = null,
)