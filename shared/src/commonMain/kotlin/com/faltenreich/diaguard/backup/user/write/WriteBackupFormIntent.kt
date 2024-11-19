package com.faltenreich.diaguard.backup.user.write

sealed interface WriteBackupFormIntent {

    data object Confirm : WriteBackupFormIntent
}