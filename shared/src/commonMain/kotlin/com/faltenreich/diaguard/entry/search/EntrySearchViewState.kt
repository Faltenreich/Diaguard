package com.faltenreich.diaguard.entry.search

import com.faltenreich.diaguard.entry.Entry

sealed class EntrySearchViewState(val query: String) {

    data object Idle: EntrySearchViewState("")

    class Loading(query: String): EntrySearchViewState(query)

    class Error(query: String): EntrySearchViewState(query)

    class Result(query: String, val entries: List<Entry>): EntrySearchViewState(query)
}