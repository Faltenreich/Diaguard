package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.entry.Entry

sealed class Screen {

    object Dashboard : Screen()

    data class EntryForm(val entry: Entry) : Screen()
}