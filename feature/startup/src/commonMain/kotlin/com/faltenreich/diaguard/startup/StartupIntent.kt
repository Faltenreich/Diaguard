package com.faltenreich.diaguard.startup

sealed interface StartupIntent {

    data object MigrateData : StartupIntent
}