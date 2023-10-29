package com.faltenreich.diaguard.entry.search

import com.faltenreich.diaguard.entry.Entry

sealed class EntrySearchViewState(val items: List<Entry>?) {

    data object Idle: EntrySearchViewState(null)

    data object Loading: EntrySearchViewState(null)

    class Result(items: List<Entry>): EntrySearchViewState(items)
}