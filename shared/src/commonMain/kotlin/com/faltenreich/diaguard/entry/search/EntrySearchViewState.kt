package com.faltenreich.diaguard.entry.search

import com.faltenreich.diaguard.entry.Entry

sealed interface EntrySearchViewState {

    data object Idle: EntrySearchViewState

    data object Loading: EntrySearchViewState

    data class Result(val items: List<Entry.Local>?): EntrySearchViewState
}