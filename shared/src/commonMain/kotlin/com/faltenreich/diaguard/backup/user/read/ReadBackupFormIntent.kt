package com.faltenreich.diaguard.backup.user.read

sealed interface ReadBackupFormIntent {

    data object Confirm : ReadBackupFormIntent
}