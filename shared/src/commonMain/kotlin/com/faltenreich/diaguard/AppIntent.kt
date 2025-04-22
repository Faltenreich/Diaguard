package com.faltenreich.diaguard

sealed interface AppIntent {

    data object MigrateData : AppIntent
}