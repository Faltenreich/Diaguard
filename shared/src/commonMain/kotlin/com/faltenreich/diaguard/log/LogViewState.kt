package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.Month

data class LogViewState(
    val currentDate: Date,
    val data: Map<Month, List<LogData>>,
    val scrollPosition: Int? = null,
)