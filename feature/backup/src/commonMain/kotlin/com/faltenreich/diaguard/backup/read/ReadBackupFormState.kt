package com.faltenreich.diaguard.backup.read

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

sealed interface ReadBackupFormState {

    data object Idle : ReadBackupFormState

    data object Selected : ReadBackupFormState

    data object Reading : ReadBackupFormState

    data object Ready : ReadBackupFormState

    data object Checked : ReadBackupFormState

    data object Storing : ReadBackupFormState

    data object Completed : ReadBackupFormState

    class PreviewParameter : PreviewParameterProvider<ReadBackupFormState> {

        override val values = sequenceOf(
            Idle,
            Selected,
            Reading,
            Ready,
            Checked,
            Storing,
            Completed,
        )
    }
}