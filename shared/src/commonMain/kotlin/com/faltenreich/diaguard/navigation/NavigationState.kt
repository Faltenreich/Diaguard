package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.entry.Entry

sealed class NavigationState {

    object Dashboard : NavigationState()

    data class EntryForm(val entry: Entry) : NavigationState()
}