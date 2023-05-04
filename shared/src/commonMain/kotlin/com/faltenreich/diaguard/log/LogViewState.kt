package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.entry.Entry

sealed class LogViewState {

    object Requesting : LogViewState()

    class Responding(
        val entries: List<Entry> = emptyList(),
    ) : LogViewState()
}