package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.shared.datetime.Date

data class LogPaginationState(
    val minimumDate: Date,
    val maximumDate: Date,
)