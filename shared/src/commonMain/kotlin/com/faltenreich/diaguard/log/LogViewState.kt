package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.shared.datetime.MonthOfYear

data class LogViewState(
    val items: Map<MonthOfYear, List<LogItem>>,
    val scrollPosition: Int? = null,
)