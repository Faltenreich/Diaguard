package com.faltenreich.diaguard.entry.list

import com.faltenreich.diaguard.entry.Entry

sealed interface EntryListIntent {

    data class OpenEntry(val entry: Entry) : EntryListIntent

    data class DeleteEntry(val entry: Entry) : EntryListIntent
}