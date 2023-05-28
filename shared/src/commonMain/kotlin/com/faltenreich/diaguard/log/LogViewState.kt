package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.MonthOfYear

data class LogViewState(
    val currentDate: Date,
    val items: Map<MonthOfYear, List<LogItem>>,
    val scrollPosition: Int? = null,
)