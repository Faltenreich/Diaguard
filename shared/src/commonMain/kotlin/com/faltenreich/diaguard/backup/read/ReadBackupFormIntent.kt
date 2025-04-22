package com.faltenreich.diaguard.backup.read

sealed interface ReadBackupFormIntent {

    data object Select : ReadBackupFormIntent

    data object Read : ReadBackupFormIntent

    data object Check : ReadBackupFormIntent

    data object Store : ReadBackupFormIntent
}