package com.faltenreich.diaguard.log.list.item

import com.faltenreich.diaguard.datetime.Date

data class LogDayState(
    val date: Date,
    val dayOfMonthLocalized: String,
    val dayOfWeekLocalized: String,
    val style: LogDayStyle,
)