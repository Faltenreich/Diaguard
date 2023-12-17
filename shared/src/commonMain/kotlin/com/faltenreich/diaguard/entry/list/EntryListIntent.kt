package com.faltenreich.diaguard.entry.list

import com.faltenreich.diaguard.entry.Entry

sealed interface EntryListIntent {

    data class Delete(val entry: Entry) : EntryListIntent
}