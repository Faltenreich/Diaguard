package com.faltenreich.diaguard.backup.user.read

sealed interface ReadBackupFormState {

    data object Idle : ReadBackupFormState

    data object Ready : ReadBackupFormState

    data object Loading : ReadBackupFormState

    data object Completed : ReadBackupFormState

    data object Error : ReadBackupFormState
}