package com.faltenreich.diaguard.entry.delete

sealed interface EntryDeleteIntent {

    data object Close : EntryDeleteIntent

    data object Confirm : EntryDeleteIntent
}