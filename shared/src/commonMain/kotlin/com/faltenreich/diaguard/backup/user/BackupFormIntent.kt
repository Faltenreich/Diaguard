package com.faltenreich.diaguard.backup.user

sealed interface BackupFormIntent {

    data object Read : BackupFormIntent

    data object Write : BackupFormIntent
}