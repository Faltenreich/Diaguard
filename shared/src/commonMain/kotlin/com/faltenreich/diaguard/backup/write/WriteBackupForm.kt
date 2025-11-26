package com.faltenreich.diaguard.backup.write

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.backup_write_completed
import com.faltenreich.diaguard.resource.backup_write_idle
import com.faltenreich.diaguard.resource.backup_write_loading
import com.faltenreich.diaguard.resource.backup_write_summary
import com.faltenreich.diaguard.resource.retry
import com.faltenreich.diaguard.resource.start
import com.faltenreich.diaguard.resource.store
import com.faltenreich.diaguard.view.layout.WizardStepListItem
import com.faltenreich.diaguard.view.layout.WizardStepState
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun WriteBackupForm(
    state: WriteBackupFormState?,
    onIntent: (WriteBackupFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = state ?: WriteBackupFormState.Idle

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = AppTheme.dimensions.padding.P_3_5,
                vertical = AppTheme.dimensions.padding.P_3,
            ),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_4),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(Res.string.backup_write_summary))

        WizardStepListItem(
            index = 0,
            label = stringResource(Res.string.backup_write_idle),
            state =
                if (state == WriteBackupFormState.Idle) WizardStepState.CURRENT
                else WizardStepState.COMPLETED,
        ) {
            Button(onClick = { onIntent(WriteBackupFormIntent.Start) }) {
                Text(stringResource(Res.string.start))
            }
        }

        WizardStepListItem(
            index = 1,
            label = stringResource(Res.string.backup_write_loading),
            state = when (state) {
                WriteBackupFormState.Idle -> WizardStepState.UPCOMING
                WriteBackupFormState.Loading -> WizardStepState.CURRENT
                WriteBackupFormState.Completed,
                WriteBackupFormState.Error -> WizardStepState.COMPLETED
            },
        ) {
            CircularProgressIndicator(
                color = AppTheme.colors.scheme.onPrimaryContainer,
            )
        }

        WizardStepListItem(
            index = 2,
            label = stringResource(Res.string.backup_write_completed),
            state = when (state) {
                WriteBackupFormState.Idle,
                WriteBackupFormState.Loading -> WizardStepState.UPCOMING
                WriteBackupFormState.Completed,
                WriteBackupFormState.Error -> WizardStepState.CURRENT
            },
        ) {
            if (state == WriteBackupFormState.Completed) {
                Button(onClick = { onIntent(WriteBackupFormIntent.Store) }) {
                    Text(stringResource(Res.string.store))
                }
            } else {
                Button(onClick = { onIntent(WriteBackupFormIntent.Start) }) {
                    Text(stringResource(Res.string.retry))
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    WriteBackupForm(
        // TODO: Add missing states when PreviewParameter is working
        state = WriteBackupFormState.Idle,
        onIntent = {},
    )
}