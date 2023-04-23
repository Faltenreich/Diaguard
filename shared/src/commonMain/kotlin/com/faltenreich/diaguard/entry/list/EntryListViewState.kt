package com.faltenreich.diaguard.entry.list

import com.faltenreich.diaguard.entry.Entry

data class EntryListViewState(
    val entries: List<Entry> = emptyList(),
)