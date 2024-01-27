package com.faltenreich.diaguard.entry.search

import com.faltenreich.diaguard.entry.Entry

sealed interface EntrySearchIntent {

    data class OpenEntry(val entry: Entry) : EntrySearchIntent
}