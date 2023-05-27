package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.shared.datetime.Date

data class LogViewState(
    val currentDate: Date,
    val data: List<LogData>,
    val scrollPosition: Int? = null,
)