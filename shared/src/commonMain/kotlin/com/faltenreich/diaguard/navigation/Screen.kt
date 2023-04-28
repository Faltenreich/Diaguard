package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.entry.Entry

sealed class Screen {

    object Dashboard : Screen()

    object Timeline : Screen()

    object Log : Screen()

    data class EntryForm(val entry: Entry) : Screen()
}