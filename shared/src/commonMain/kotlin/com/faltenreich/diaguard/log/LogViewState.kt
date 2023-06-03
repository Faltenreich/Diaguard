package com.faltenreich.diaguard.log

import app.cash.paging.PagingData
import com.faltenreich.diaguard.log.item.LogItem

data class LogViewState(
    val items: PagingData<LogItem>,
    val scrollPosition: Int? = null,
)