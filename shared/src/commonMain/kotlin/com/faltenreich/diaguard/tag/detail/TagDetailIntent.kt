package com.faltenreich.diaguard.tag.detail

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.list.EntryListItemState

sealed interface TagDetailIntent {

    data class SetName(val name: String) : TagDetailIntent

    data object UpdateTag : TagDetailIntent

    data object OpenDeleteDialog : TagDetailIntent

    data object CloseDeleteDialog : TagDetailIntent

    data object DeleteTag : TagDetailIntent

    data class OpenEntry(val entry: Entry.Local) : TagDetailIntent

    data class DeleteEntry(val entry: Entry.Local) : TagDetailIntent

    data class RestoreEntry(val entry: EntryListItemState) : TagDetailIntent

    data class OpenEntrySearch(val query: String) : TagDetailIntent
}