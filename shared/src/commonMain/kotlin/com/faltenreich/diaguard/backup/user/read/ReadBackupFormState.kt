package com.faltenreich.diaguard.backup.user.read

sealed interface ReadBackupFormState {

    data object Idle : ReadBackupFormState

    data object Selected : ReadBackupFormState

    data object Reading : ReadBackupFormState

    data object Ready : ReadBackupFormState

    data object Checked : ReadBackupFormState

    data object Storing : ReadBackupFormState

    data object Completed : ReadBackupFormState

    data object Error : ReadBackupFormState
}