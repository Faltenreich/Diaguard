package com.faltenreich.diaguard.backup.user.write

sealed interface WriteBackupFormIntent {

    data object Start : WriteBackupFormIntent

    data object Store : WriteBackupFormIntent

    data object Restart : WriteBackupFormIntent
}