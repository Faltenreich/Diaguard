package com.faltenreich.diaguard.backup.user.write

sealed interface WriteBackupFormState {

    data object Idle : WriteBackupFormState

    data object Loading : WriteBackupFormState

    data object Complete : WriteBackupFormState

    data object Error : WriteBackupFormState
}