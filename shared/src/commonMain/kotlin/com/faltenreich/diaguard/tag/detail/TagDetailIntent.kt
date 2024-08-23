package com.faltenreich.diaguard.tag.detail

import com.faltenreich.diaguard.entry.Entry

sealed interface TagDetailIntent {

    data object UpdateTag : TagDetailIntent

    data object DeleteTag : TagDetailIntent

    data class OpenEntry(val entry: Entry.Localized) : TagDetailIntent

    data class OpenEntrySearch(val query: String) : TagDetailIntent
}