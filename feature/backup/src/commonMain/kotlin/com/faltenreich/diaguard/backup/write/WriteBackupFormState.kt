package com.faltenreich.diaguard.backup.write

import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

sealed interface WriteBackupFormState {

    data object Idle : WriteBackupFormState

    data object Loading : WriteBackupFormState

    data object Completed : WriteBackupFormState

    data object Error : WriteBackupFormState

    class PreviewParameter : PreviewParameterProvider<WriteBackupFormState> {

        override val values = sequenceOf(
            Idle,
            Loading,
            Completed,
            Error,
        )
    }
}