package com.faltenreich.diaguard.backup.user.read

sealed interface ReadBackupFormIntent {

    data object Select : ReadBackupFormIntent

    data object Read : ReadBackupFormIntent

    data object Check : ReadBackupFormIntent

    data object Store : ReadBackupFormIntent
}