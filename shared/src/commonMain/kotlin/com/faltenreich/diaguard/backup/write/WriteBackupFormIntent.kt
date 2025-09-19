package com.faltenreich.diaguard.backup.write

sealed interface WriteBackupFormIntent {

    data object Start : WriteBackupFormIntent

    data object Store : WriteBackupFormIntent
}