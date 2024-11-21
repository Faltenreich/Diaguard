package com.faltenreich.diaguard.backup.user.read

sealed interface ReadBackupFormIntent {

    data object Select : ReadBackupFormIntent

    data object Start : ReadBackupFormIntent
}