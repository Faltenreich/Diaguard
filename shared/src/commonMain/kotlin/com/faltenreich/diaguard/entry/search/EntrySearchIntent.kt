package com.faltenreich.diaguard.entry.search

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.entry.list.EntryListItemState

sealed interface EntrySearchIntent {

    data class SetQuery(val query: String) : EntrySearchIntent

    data class OpenEntry(val entry: Entry.Local) : EntrySearchIntent

    data class DeleteEntry(val entry: Entry.Local) : EntrySearchIntent

    data class RestoreEntry(val entry: EntryListItemState) : EntrySearchIntent
}