package com.faltenreich.diaguard.entry.list

import com.faltenreich.diaguard.entry.Entry

data class EntryListItemState(
    val entry: Entry.Local,
    val dateTimeLocalized: String,
    val foodEatenLocalized: List<String>,
)