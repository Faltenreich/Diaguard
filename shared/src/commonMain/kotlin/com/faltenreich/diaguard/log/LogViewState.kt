package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.entry.Entry

data class LogViewState(
    val entries: List<Entry> = emptyList(),
)