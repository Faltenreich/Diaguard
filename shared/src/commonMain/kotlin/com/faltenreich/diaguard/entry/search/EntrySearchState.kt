package com.faltenreich.diaguard.entry.search

import com.faltenreich.diaguard.entry.Entry

sealed interface EntrySearchState {

    data object Idle: EntrySearchState

    data object Loading: EntrySearchState

    data class Result(val items: List<Entry.Localized>?): EntrySearchState
}